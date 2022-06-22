package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.Int32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import org.khronos.webgl.WebGLTexture
import kotlin.reflect.KProperty

interface Texture {
	abstract val glTexture : WebGLTexture?
}

interface Uniform {
	fun set(other: Uniform) : Uniform
	fun set(vararg values : Float) : Uniform
	fun set(firstTexture : Texture, vararg moreTextures : Texture)
	fun getStorageSize() : Int

	fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int = 0)
}

interface UniformFloat : Uniform {
	abstract val storage : Float32Array

	override fun getStorageSize() : Int{
		return storage.length
	}

	override fun set(other: Uniform) : Uniform {
		if(other is UniformFloat){
			for(i : Int in 0 until storage.length) {
	    		storage[i] = if(i < other.storage.length) other.storage[i] else 0.0f
	    }
    } else {
			throw Error("Cannot set a uniform of floats from a non-float uniform.")
    }
    return this
	}

	override fun set(firstTexture : Texture, vararg moreTextures : Texture){
		throw Error("Cannot set a texture to a non-sampler uniform.")
	}

}

interface UniformSampler : Uniform {
	abstract val storage : Int32Array
	abstract val glTextures : Array<WebGLTexture?>

	override fun getStorageSize() : Int{
		return storage.length
	}

	override fun set(other: Uniform) : Uniform {
		if(other is UniformSampler){
			for(i : Int in 0 until storage.length) {
	    		storage[i] = if(i < other.storage.length) other.storage[i] else 0
	    		glTextures[i] = other.glTextures.getOrNull(i)
	    }
    } else {
			throw Error("Cannot set a uniform of textures from a non-texture uniform.")
    }
    return this
	}

	override fun set(firstTexture : Texture, vararg moreTextures : Texture){
		glTextures[0] = firstTexture.glTexture
		for(i : Int in 0 until storage.length) {
			glTextures[i+1] = moreTextures.getOrNull(i)?.glTexture
		}
	}

	override fun set(vararg values : Float) : UniformFloat {
		throw Error("Cannot set float values to a texture uniform.")
	}
}

