import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.WebGLTexture
import vision.gears.webglmath.Texture

class RenderTargetTexture(
  gl : WebGL2RenderingContext,
  width : Int = 512,
  height : Int = 512,
  internalFormat : Int = GL.RGBA,
  format : Int = internalFormat,
  type : Int = GL.UNSIGNED_BYTE
  ) : Texture {
  override val glTexture : WebGLTexture? = gl.createTexture()

  init {
    gl.bindTexture(GL.TEXTURE_2D, glTexture)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_S, GL.CLAMP_TO_EDGE)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_WRAP_T, GL.CLAMP_TO_EDGE)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MIN_FILTER, GL.NEAREST)
    gl.texParameteri(GL.TEXTURE_2D, GL.TEXTURE_MAG_FILTER, GL.NEAREST)
    gl.texImage2D(GL.TEXTURE_2D, 0, internalFormat, width, height, 0,
       format, type, null)
  }
}

