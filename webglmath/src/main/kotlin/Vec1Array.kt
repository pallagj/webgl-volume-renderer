package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.reflect.KProperty
import kotlin.random.Random
import kotlin.math.pow
import kotlin.math.sqrt

class Vec1Array(backingStorage: Float32Array?, startIndex: Int = 0, endIndex: Int = 0) : VecArray() {

  constructor(size : Int) : this(null, size, size) {}

  override val storage = backingStorage?.subarray(startIndex, endIndex)?:Float32Array(startIndex)

  override fun set(vararg values : Float) : Vec1Array {
    for(i in 0 until storage.length) {
      storage[i] = values.getOrNull(i) ?: 0.0f
    }
    return this
  }

  operator fun get(i : Int) : Vec1{
    return Vec1(storage, i)
  }

  fun subarray(begin : Int, end : Int) : Vec1Array {
    return Vec1Array(storage, begin, end)
  }

  fun setDot(b : Vec2Array, c : Vec2Array) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*2] * c.storage[i*2] + b.storage[i*2+1] * c.storage[i*2+1]
    } 
  }
  fun setDot(b : Vec3Array, c : Vec3Array) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*3] * c.storage[i*3] + b.storage[i*3+1] * c.storage[i*3+1] + b.storage[i*3+2] * c.storage[i*3+2]
    }
  }
  fun setDot(b : Vec4Array, c : Vec4Array) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*4] * c.storage[i*4] + b.storage[i*4+1] * c.storage[i*4+1] + b.storage[i*4+2] * c.storage[i*4+2] + b.storage[i*4+3] * c.storage[i*4+3]
    }
  }  
  fun setDot(b : Vec2Array, c : Vec2) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*2] * c.storage[0] + b.storage[i*2+1] * c.storage[1]
    }
  }
  fun setDot(b : Vec3Array, c : Vec3) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*3] * c.storage[0] + b.storage[i*3+1] * c.storage[1] + b.storage[i*3+2] * c.storage[2]
    }
  }
  fun setDot(b : Vec4Array, c : Vec4) {
    for(i in 0 until storage.length) {
      storage[i] = b.storage[i*4] * c.storage[0] + b.storage[i*4+1] * c.storage[1] + b.storage[i*4+2] * c.storage[2] + b.storage[i*4+3] * c.storage[3]
    }
  }
  fun setLength(b : Vec2Array) {
    for(i in 0 until storage.length) {
      storage[i] = sqrt(b.storage[i*2] * b.storage[i*2] + b.storage[i*2+1] * b.storage[i*2+1])
    }
  }
  fun setLength(b : Vec3Array) {
    for(i in 0 until storage.length) {
      storage[i] = sqrt(b.storage[i*3] * b.storage[i*3] + b.storage[i*3+1] * b.storage[i*3+1] + b.storage[i*3+2] * b.storage[i*3+2])
    }
  }
  fun setLength(b : Vec4Array) {
    for(i in 0 until storage.length) {
      storage[i] = sqrt(b.storage[i*4] * b.storage[i*4] + b.storage[i*4+1] * b.storage[i*4+1] + b.storage[i*4+2] * b.storage[i*4+2] + b.storage[i*4+3] * b.storage[i*4+3])
    }
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Vec1Array {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Vec1Array {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Vec1Array) {
    set(value)
  }  

  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniform1fv(uniformLocation, storage);
  }  
} 