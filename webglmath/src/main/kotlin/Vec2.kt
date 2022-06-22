package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.reflect.KProperty
import kotlin.random.Random
import kotlin.math.sqrt
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.*
import kotlinx.serialization.json.*
import kotlinx.serialization.encoding.*

object Vec2Serializer : KSerializer<Vec2> {
  @kotlinx.serialization.InternalSerializationApi
  override val descriptor: SerialDescriptor =
    buildSerialDescriptor("vision.gears.Vec2", StructureKind.LIST)
  override fun deserialize(decoder: Decoder): Vec2 {
    val input = decoder as? JsonDecoder ?: throw SerializationException("Expected Json Input")
    val array = input.decodeJsonElement() as? JsonArray ?: throw SerializationException("Expected JsonArray")
    return Vec2(
      (array[0] as? JsonPrimitive)?.float ?: 0.0f,
      (array[1] as? JsonPrimitive)?.float ?: 0.0f 
    )
    //return Vec2(decoder.decodeSerializableValue(FloatArraySerializer()))
  }
  override fun serialize(encoder: Encoder, value : Vec2) {
    val output = encoder as? JsonEncoder ?: throw SerializationException("This class can be saved only by Json")
    val array = buildJsonArray {
      add(value.x)
      add(value.y)
    }
    /*array.encodeFloatElement(descriptor, 0, value.x)
    array.encodeFloatElement(descriptor, 1, value.y)*/
    output.encodeJsonElement(array)
  }      
}


@Suppress("NOTHING_TO_INLINE")
@Serializable(with = Vec2Serializer::class)
class Vec2(backingStorage: Float32Array?, offset: Int = 0) : UniformFloat {

  constructor(u: Float = 0.0f, v: Float = 0.0f) : this(null, 0){
    storage[0] = u
    storage[1] = v
  }
  constructor(other : Vec1, v: Float = 0.0f) : this(other.storage[0], v ){}
  constructor(other : Vec2) : this(null, 0)  {
    storage.set(other.storage);
  }

  override val storage: Float32Array = backingStorage?.subarray(offset, offset+2)?:Float32Array(2)
  inline var x : Float
    get() = storage[0]
    set(value) { storage[0] = value }
  inline var y : Float
    get() = storage[1]
    set(value) { storage[1] = value }
  inline val xy0 : Vec3
    get() = Vec3(storage[0], storage[1], 0.0f)
  inline val xy00 : Vec4
    get() = Vec4(storage[0], storage[1], 0.0f, 0.0f)    
  inline val xy01 : Vec4
    get() = Vec4(storage[0], storage[1], 0.0f, 1.0f)

  inline fun clone() : Vec2 {
    return Vec2(this);
  }

  override fun set(vararg values : Float) : Vec2 {
    storage[0] = values.getOrElse(0) {0.0f}
    storage[1] = values.getOrElse(1) {0.0f}
    return this 
  }

  companion object {
    val zeros = Vec2()
    val ones = Vec2(1.0f, 1.0f) 

    inline fun makeRandom(minVal: Vec2 = Vec2.zeros, maxVal: Vec2 = Vec2.ones) : Vec2 {
      return Vec2(
          Random.nextFloat() * (maxVal.storage[0] - minVal.storage[0]) + minVal.storage[0],
          Random.nextFloat() * (maxVal.storage[1] - minVal.storage[1]) + minVal.storage[1]
        )  
    }
    inline fun makeRandom(minVal: Float = 0.0f, maxVal: Float = 1.0f) : Vec2 {
      return Vec2.makeRandom(Vec2(minVal, minVal), Vec2(maxVal, maxVal))
    }
  }

  inline fun randomize(minVal: Vec2 = Vec2.zeros, maxVal: Vec2 = Vec2.ones){
    set(Vec2.makeRandom(minVal, maxVal))
  }
  inline fun randomize(minVal: Float = 0.0f, maxVal: Float = 1.0f){
    randomize(Vec2(minVal, minVal), Vec2(maxVal, maxVal))
  }

  inline fun clamp(minVal: Vec2 = Vec2.zeros, maxVal: Vec2 = Vec2.ones) : Vec2 {
    if(storage[0] < minVal.storage[0]){
      storage[0] = minVal.storage[0]
    }
    if(storage[1] < minVal.storage[1]){
      storage[1] = minVal.storage[1]
    }
    if(storage[0] > maxVal.storage[0]){
      storage[0] = maxVal.storage[0]
    }
    if(storage[1] > maxVal.storage[1]){
      storage[1] = maxVal.storage[1]
    }
    return this
  }

  operator inline fun unaryPlus() : Vec2 {
    return this
  }

  operator inline fun unaryMinus() : Vec2 {
    return Vec2(-storage[0], -storage[1])
  }

  operator inline fun times(scalar : Float) : Vec2 {
    return Vec2(
      storage[0] * scalar,
      storage[1] * scalar
      )
  }

  operator inline fun div(scalar : Float) : Vec2 {
    return Vec2(
      storage[0] / scalar,
      storage[1] / scalar      
      )
  }

  operator inline fun timesAssign(scalar : Float) {
    storage[0] *= scalar
    storage[1] *= scalar
  }

  operator inline fun divAssign(scalar : Float) {
    storage[0] /= scalar
    storage[1] /= scalar
  }

  operator inline fun plusAssign(other : Vec2) {
    storage[0] += other.storage[0]
    storage[1] += other.storage[1]
  }

  operator inline fun plus(other : Vec2) : Vec2 {
    return Vec2(
      storage[0] + other.storage[0],
      storage[1] + other.storage[1]
      )
  }

  operator inline fun minusAssign(other : Vec2) {
    storage[0] -= other.storage[0]
    storage[1] -= other.storage[1]
  }

  operator inline fun minus(other : Vec2) : Vec2 {
    return Vec2(
      storage[0] - other.storage[0],
      storage[1] - other.storage[1]
      )
  }

  operator inline fun timesAssign(other : Vec2) {
    storage[0] *= other.storage[0]
    storage[1] *= other.storage[1]
  }

  operator inline fun times(other : Vec2) : Vec2 {
    return Vec2(
      storage[0] * other.storage[0],
      storage[1] * other.storage[1]
      )
  }

  operator inline fun divAssign(other : Vec2) {
    storage[0] /= other.storage[0]
    storage[1] /= other.storage[1]
  }

  operator inline fun div(other : Vec2) : Vec2 {
    return Vec2(
      storage[0] / other.storage[0],
      storage[1] / other.storage[1]
      )
  }       

  inline fun lengthSquared() : Float {
    return storage[0] * storage[0] + storage[1] * storage[1]
  }

  inline fun length() : Float {
    return sqrt(lengthSquared());
  }

  inline fun normalize() : Vec2 {
    val l = this.length()
    storage[0] /= l
    storage[1] /= l
    return this
  }

  infix inline fun dot(other : Vec2) : Float {
    return (
     storage[0] * other.storage[0] +
     storage[1] * other.storage[1] )
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Vec2 {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Vec2 {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Vec2) {
    set(value)
  }
  
  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniform2fv(uniformLocation, storage);
  }
}

@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.times(v: Vec2) = Vec2(this * v.storage[0], this * v.storage[1])
@Suppress("NOTHING_TO_INLINE")
operator inline fun Float.div(v: Vec2) = Vec2(this / v.storage[0], this / v.storage[1])
