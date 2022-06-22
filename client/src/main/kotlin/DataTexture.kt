import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.WebGLTexture
import org.khronos.webgl.ArrayBufferView
import vision.gears.webglmath.Texture

class DataTexture(
  gl : WebGL2RenderingContext,
  dataArray : ArrayBufferView,
  width : Int = 64,
  height : Int = 1,
  internalFormat : Int = GL.RGBA32F,
  format : Int = GL.RGBA,
  type : Int = GL.FLOAT
  ) : Texture {
  override val glTexture : WebGLTexture? = gl.createTexture()

  init {
    gl.bindTexture(GL.TEXTURE_2D, glTexture)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_S, GL.CLAMP_TO_EDGE)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_T, GL.CLAMP_TO_EDGE)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.NEAREST)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.NEAREST)
    gl.texImage2D(GL.TEXTURE_2D, 0, internalFormat, width, height, 0,
       format, type, dataArray)
  }
}

