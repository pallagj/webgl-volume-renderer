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

class Vec4Array(backingStorage: Float32Array?, startIndex: Int = 0, endIndex: Int = 0) : VecArray() {

  constructor(size : Int) : this(null, size, size) {}

  override val storage = backingStorage?.subarray(startIndex*4, endIndex*4)?:Float32Array(startIndex*4)

  override fun set(vararg values : Float) : Vec4Array {
    for(i in 0 until storage.length) {
      storage[i] = values.getOrNull(i%4) ?: if(i%4 == 3) 1.0f else 0.0f
    }
    return this
  }

  operator fun get(i : Int) : Vec4{
    return Vec4(storage, i*4)
  }

  fun subarray(begin : Int, end : Int) : Vec4Array {
    return Vec4Array(storage, begin*4, end*4)
  }

  fun setNormalized(b : Vec4Array) {
    for(i in 0 until storage.length step 4) {
      val l2 =
        b.storage[i  ] * b.storage[i  ] +
        b.storage[i+1] * b.storage[i+1] +
        b.storage[i+2] * b.storage[i+2] +
        b.storage[i+3] * b.storage[i+3]        
      val linv = 1 / sqrt(l2)
      storage[i  ] = b.storage[i  ] * linv
      storage[i+1] = b.storage[i+1] * linv
      storage[i+2] = b.storage[i+2] * linv
      storage[i+3] = b.storage[i+3] * linv      
    }
  }

  fun setTransformed(v : Vec4Array, m : Mat4) {  
    for(i in 0 until storage.length step 4) {
      storage[i+0] =
          v.storage[i+0] * m.storage[ 0] +
          v.storage[i+1] * m.storage[ 1] +
          v.storage[i+2] * m.storage[ 2] +
          v.storage[i+3] * m.storage[ 3]
      storage[i+1] =
          v.storage[i+0] * m.storage[ 4] +
          v.storage[i+1] * m.storage[ 5] +
          v.storage[i+2] * m.storage[ 6] +
          v.storage[i+3] * m.storage[ 7]
      storage[i+2] =
          v.storage[i+0] * m.storage[ 8] +
          v.storage[i+1] * m.storage[ 9] +
          v.storage[i+2] * m.storage[10] +
          v.storage[i+3] * m.storage[11]
      storage[i+3] =
          v.storage[i+0] * m.storage[12] +
          v.storage[i+1] * m.storage[13] +
          v.storage[i+2] * m.storage[14] +
          v.storage[i+3] * m.storage[15]                
    }
  }

  fun transformNormal(v : Vec3Array, m : Mat4) {  
    for(i in 0 until storage.length step 3) {
      storage[i+0] =
         v.storage[i+0] * m.storage[ 0] +
         v.storage[i+1] * m.storage[ 1] +
         v.storage[i+2] * m.storage[ 2]
      storage[i+1] =
         v.storage[i+0] * m.storage[ 4] +
         v.storage[i+1] * m.storage[ 5] +
         v.storage[i+2] * m.storage[ 6]
      storage[i+2] =
         v.storage[i+0] * m.storage[ 8] +
         v.storage[i+1] * m.storage[ 9] +
         v.storage[i+2] * m.storage[10]      
    }
  }  

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Vec4Array {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Vec4Array {
    return this
  }
  
  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Vec4Array) {
    set(value)
  }    

  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniform4fv(uniformLocation, storage);
  }  
} 