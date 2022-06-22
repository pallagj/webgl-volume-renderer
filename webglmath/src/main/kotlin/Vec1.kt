package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.reflect.KProperty
import kotlin.random.Random

@Suppress("NOTHING_TO_INLINE")
class Vec1(backingStorage: Float32Array?, offset: Int = 0) : UniformFloat {

  constructor(u: Float = 0.0f) : this(null, 0){
    storage[0] = u
  }
  constructor(other : Vec1) : this(null, 0)  {
    storage.set(other.storage);
  }

  override val storage: Float32Array = backingStorage?.subarray(offset, offset+1)?:Float32Array(1)
  inline var x : Float
    get() = storage[0]
    set(value) { storage[0] = value }
  inline val xx : Vec2
    get() = Vec2(storage[0], storage[0])
  inline val xxx : Vec3
    get() = Vec3(storage[0], storage[0], storage[0])
  inline val xxxx : Vec4
    get() = Vec4(storage[0], storage[0], storage[0], storage[0])        

  inline fun clone() : Vec1 {
    return Vec1(this);
  }

  override fun set(vararg values : Float) : Vec1 {
    storage[0] = values.getOrElse(0) {0.0f}
    return this 
  }

  companion object {
    val zeros = Vec1()
    val ones = Vec1(1.0f) 

    inline fun makeRandom(minVal: Vec1 = Vec1.zeros, maxVal: Vec1 = Vec1.ones) : Vec1 {
      return Vec1(
          Random.nextFloat() * (maxVal.storage[0] - minVal.storage[0]) + minVal.storage[0]                    
        )  
    }
    inline fun makeRandom(minVal: Float = 0.0f, maxVal: Float = 1.0f): Vec1 {
      return Vec1.makeRandom(Vec1(minVal), Vec1(maxVal))
    }
  }

  inline fun randomize(minVal: Vec1 = Vec1.zeros, maxVal: Vec1 = Vec1.ones){
    set(Vec1.makeRandom(minVal, maxVal))
  }
  inline fun randomize(minVal: Float = 0.0f, maxVal: Float = 1.0f){
    randomize(Vec1(minVal), Vec1(maxVal))
  }

  inline fun clamp(minVal: Vec1 = Vec1.zeros, maxVal: Vec1 = Vec1.ones) : Vec1 {
    if(storage[0] < minVal.storage[0]){
      storage[0] = minVal.storage[0]
    }
    if(storage[0] > maxVal.storage[0]){
      storage[0] = maxVal.storage[0]
    }
    return this
  }

  operator inline fun unaryPlus() : Vec1 {
    return this
  }

  operator inline fun unaryMinus() : Vec1 {
    return Vec1(-storage[0])
  }

  operator inline fun times(scalar : Float) : Vec1 {
    return Vec1(
      storage[0] * scalar
      )
  }

  operator inline fun div(scalar : Float) : Vec1 {
    return Vec1(
      storage[0] / scalar
      )
  }

  operator inline fun timesAssign(scalar : Float) {
    storage[0] *= scalar
  }

  operator inline fun divAssign(scalar : Float) {
    storage[0] /= scalar
  }    

  operator inline fun plusAssign(other : Vec1) {
    storage[0] += other.storage[0]
  }

  operator inline fun plus(other : Vec1) : Vec1 {
    return Vec1(
      storage[0] + other.storage[0]
      )
  }

  operator inline fun minusAssign(other : Vec1) {
    storage[0] -= other.storage[0]
  }

  operator inline fun minus(other : Vec1) : Vec1 {
    return Vec1(
      storage[0] - other.storage[0]
      )
  }

  operator inline fun timesAssign(other : Vec1) {
    storage[0] *= other.storage[0]
  }

  operator inline fun times(other : Vec1) : Vec1 {
    return Vec1(
      storage[0] * other.storage[0]
      )
  }

  operator inline fun divAssign(other : Vec1) {
    storage[0] /= other.storage[0]
  }

  operator inline fun div(other : Vec1) : Vec1 {
    return Vec1(
      storage[0] / other.storage[0]
      )
  }       

  inline fun lengthSquared() : Float {
    return storage[0] * storage[0]
  }

  inline fun length() : Float {
    return storage[0];
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Vec1 {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Vec1 {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Vec1) {
    set(value)
  }

  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniform1fv(uniformLocation, storage);
  }
}

@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.times(v: Vec1) = Vec1(this * v.storage[0])
@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.div(v: Vec1) = Vec1(this / v.storage[0])
