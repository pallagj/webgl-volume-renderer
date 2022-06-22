package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.reflect.KProperty
import kotlin.random.Random
import kotlin.math.sqrt

@Suppress("NOTHING_TO_INLINE")
class Vec3(backingStorage: Float32Array?, offset: Int = 0) : UniformFloat {

  constructor(u: Float = 0.0f, v: Float = 0.0f, s: Float = 0.0f) : this(null, 0){
    storage[0] = u
    storage[1] = v
    storage[2] = s
  }
  constructor(other : Vec1, v: Float = 0.0f, s: Float = 0.0f) : this(other.storage[0], v, s){}
  constructor(other : Vec2, s: Float = 0.0f) : this(other.storage[0], other.storage[1], s){}
  constructor(other : Vec3) : this(null, 0)  {
    storage.set(other.storage);
  }
  constructor(other : Vec4) : this(other.storage[0]/other.storage[3], other.storage[1]/other.storage[3], other.storage[2]/other.storage[3]){}

  override val storage: Float32Array = backingStorage?.subarray(offset, offset+3)?:Float32Array(3)
  inline var x : Float
    get() = storage[0]
    set(value) { storage[0] = value }
  inline var y : Float
    get() = storage[1]
    set(value) { storage[1] = value }
  inline var z : Float
    get() = storage[2]
    set(value) { storage[2] = value }
  inline var xy : Vec2
    get() = Vec2(storage)
    set(value) { Vec2(storage).set(value) }
  inline val xyz0 : Vec4
    get() = Vec4(storage[0], storage[1], storage[2], 0.0f)    
  inline val xyz1 : Vec4
    get() = Vec4(storage[0], storage[1], storage[2], 1.0f)

  inline fun clone() : Vec3 {
    return Vec3(this);
  }

  override fun set(vararg values : Float) : Vec3 {
    storage[0] = values.getOrElse(0) {0.0f}
    storage[1] = values.getOrElse(1) {0.0f}
    storage[2] = values.getOrElse(2) {0.0f}
    return this 
  }

  companion object {
    val zeros = Vec3()
    val ones = Vec3(1.0f, 1.0f, 1.0f) 

    inline fun makeRandom(minVal: Vec3 = Vec3.zeros, maxVal: Vec3 = Vec3.ones) : Vec3 {
      return Vec3(
          Random.nextFloat() * (maxVal.storage[0] - minVal.storage[0]) + minVal.storage[0],
          Random.nextFloat() * (maxVal.storage[1] - minVal.storage[1]) + minVal.storage[1],
          Random.nextFloat() * (maxVal.storage[2] - minVal.storage[2]) + minVal.storage[2]                    
        )  
    }
    inline fun makeRandom(minVal: Float = 0.0f, maxVal: Float = 1.0f) : Vec3 {
      return Vec3.makeRandom(Vec3(minVal, minVal, minVal), Vec3(maxVal, maxVal, maxVal))
    }
  }

  inline fun randomize(minVal: Vec3 = Vec3.zeros, maxVal: Vec3 = Vec3.ones){
    set(Vec3.makeRandom(minVal, maxVal))
  }
  inline fun randomize(minVal: Float = 0.0f, maxVal: Float = 1.0f){
    randomize(Vec3(minVal, minVal, minVal), Vec3(maxVal, maxVal, maxVal))
  }

  inline fun clamp(minVal: Vec3 = Vec3.zeros, maxVal: Vec3 = Vec3.ones) : Vec3 {
    if(storage[0] < minVal.storage[0]){
      storage[0] = minVal.storage[0]
    }
    if(storage[1] < minVal.storage[1]){
      storage[1] = minVal.storage[1]
    }
    if(storage[2] < minVal.storage[2]){
      storage[2] = minVal.storage[2]
    }
    if(storage[0] > maxVal.storage[0]){
      storage[0] = maxVal.storage[0]
    }
    if(storage[1] > maxVal.storage[1]){
      storage[1] = maxVal.storage[1]
    }
    if(storage[2] > maxVal.storage[2]){
      storage[2] = maxVal.storage[2]
    }        
    return this
  }

  operator inline fun unaryPlus() : Vec3 {
    return this
  }

  operator inline fun unaryMinus() : Vec3 {
    return Vec3(-storage[0], -storage[1], -storage[2])
  }

  operator inline fun times(scalar : Float) : Vec3 {
    return Vec3(
      storage[0] * scalar,
      storage[1] * scalar,
      storage[2] * scalar
      )
  }

  operator inline fun div(scalar : Float) : Vec3 {
    return Vec3(
      storage[0] / scalar,
      storage[1] / scalar,
      storage[2] / scalar      
      )
  }

  operator inline fun timesAssign(scalar : Float) {
    storage[0] *= scalar
    storage[1] *= scalar
    storage[2] *= scalar    
  }

  operator inline fun divAssign(scalar : Float) {
    storage[0] /= scalar
    storage[1] /= scalar
    storage[2] /= scalar
  }

  operator inline fun plusAssign(other : Vec3) {
    storage[0] += other.storage[0]
    storage[1] += other.storage[1]
    storage[2] += other.storage[2]
  }

  operator inline fun plus(other : Vec3) : Vec3 {
    return Vec3(
      storage[0] + other.storage[0],
      storage[1] + other.storage[1],
      storage[2] + other.storage[2]
      )
  }

  operator inline fun minusAssign(other : Vec3) {
    storage[0] -= other.storage[0]
    storage[1] -= other.storage[1]
    storage[2] -= other.storage[2]
  }

  operator inline fun minus(other : Vec3) : Vec3 {
    return Vec3(
      storage[0] - other.storage[0],
      storage[1] - other.storage[1],
      storage[2] - other.storage[2]
      )
  }

  operator inline fun timesAssign(other : Vec3) {
    storage[0] *= other.storage[0]
    storage[1] *= other.storage[1]
    storage[2] *= other.storage[2]
  }

  operator inline fun times(other : Vec3) : Vec3 {
    return Vec3(
      storage[0] * other.storage[0],
      storage[1] * other.storage[1],
      storage[2] * other.storage[2]
      )
  }

  operator inline fun divAssign(other : Vec3) {
    storage[0] /= other.storage[0]
    storage[1] /= other.storage[1]
    storage[2] /= other.storage[2]
  }

  operator inline fun div(other : Vec3) : Vec3 {
    return Vec3(
      storage[0] / other.storage[0],
      storage[1] / other.storage[1],
      storage[2] / other.storage[2]
      )
  }       

  inline fun lengthSquared() : Float {
    return storage[0] * storage[0] + storage[1] * storage[1] + storage[2] * storage[2]
  }

  inline fun length() : Float {
    return sqrt(lengthSquared());
  }

  inline fun normalize() : Vec3 {
    val l = this.length()
    storage[0] /= l
    storage[1] /= l
    storage[2] /= l
    return this
  }

  infix inline fun dot(other : Vec3) : Float {
    return (
      storage[0] * other.storage[0] +
      storage[1] * other.storage[1] +
      storage[2] * other.storage[2] )
  }

  infix inline fun cross(other : Vec3) : Vec3 {
    return Vec3(
      storage[1] * other.storage[2] - storage[2] * other.storage[1],
      storage[2] * other.storage[0] - storage[0] * other.storage[2],
      storage[0] * other.storage[1] - storage[1] * other.storage[0])
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Vec3 {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Vec3 {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Vec3) {
    set(value)
  }

  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniform3fv(uniformLocation, storage);
  }
}

@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.times(v: Vec3) = Vec3(v.x * this, v.y * this, v.z * this)
@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.div(v: Vec3) = Vec3(this / v.x, this / v.y, this / v.z)