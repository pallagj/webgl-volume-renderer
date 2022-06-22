package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.random.Random
import kotlin.math.pow

interface Gif {
  operator fun invoke(i : Int) : Float
}

abstract class VecArray : UniformFloat, Gif {

  override operator fun invoke(i : Int) : Float{
    return storage[i % storage.length]
  }

  infix fun gets(other : Gif){
    for(i in 0 until storage.length) {
      storage[i] = other(i)
    }
  }

  operator fun plus(c : Gif) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) + c(i)
      }
    }
  }

  operator fun minus(c : Gif) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) - c(i)
      }
    }
  }

  operator fun times(c : Gif) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) * c(i)
      }
    }
  }

  operator fun div(c : Gif) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) / c(i)
      }
    }
  }      

  operator fun times(s : Float) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) * s
      }
    }
  }

  operator fun div(s : Float) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i) / s
      }
    }
  }

  fun expand(factor : Int) : Gif {
    return object : Gif {
      override operator fun invoke(i : Int) : Float {
        return this@VecArray(i / factor)
      }
    }    
  }

  operator fun plusAssign(other : Gif) {
    for(i in 0 until storage.length) {
      storage[i] += other(i)
    }
  }

  operator fun minusAssign(other : Gif) {
    for(i in 0 until storage.length) {
      storage[i] -= other(i)
    }
  }  

  operator fun timesAssign(other : Gif) {
    for(i in 0 until storage.length) {
      storage[i] *= other(i)
    }
  }

  operator fun divAssign(other : Gif) {
    for(i in 0 until storage.length) {
      storage[i] /= other(i)
    }
  }  

  operator fun timesAssign(s : Float) {
    for(i in 0 until storage.length) {
      storage[i] *= s
    }
  }

  operator fun divAssign(s : Float) {
    for(i in 0 until storage.length) {
      storage[i] /= s
    }
  }  

  fun powAssign(s : Float) {
    for(i in 0 until storage.length) {
      storage[i] = storage[i].pow(s)
    }
  }

  fun randomize() {
    for(i in 0 until storage.length) {
      storage[i] = Random.nextFloat()
    }
  }

  fun clamp() {
    for(i in 0 until storage.length) {
      if(storage[i] < 0.0f) {
        storage[i] = 0.0f
      }
      if(storage[i] > 1.0f) {
        storage[i] = 1.0f
      }
    }
  }  

}
