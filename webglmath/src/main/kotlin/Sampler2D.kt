package vision.gears.webglmath

import org.khronos.webgl.Int32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import org.khronos.webgl.WebGLTexture
import kotlin.reflect.KProperty

class Sampler2D : UniformSampler {

  override val storage = Int32Array(1)
  override val glTextures = Array<WebGLTexture?>(1) {null}

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Sampler2D {
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Sampler2D {
    return this
  }
  
  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Sampler2D) {
    set(value)
  }  

  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    storage[0] = samplerIndex;
    if(glTextures[0] != null) {
      gl.uniform1iv(uniformLocation, storage)
      gl.activeTexture(WebGLRenderingContext.TEXTURE0 + samplerIndex)
      gl.bindTexture(WebGLRenderingContext.TEXTURE_2D, glTextures[0])
    } else {
      throw Error("No texture bound to Sampler2D uniform.")
    }
  }
}