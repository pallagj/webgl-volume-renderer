import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.WebGLTexture
import org.w3c.dom.Image
import org.w3c.dom.events.Event
import vision.gears.webglmath.Texture

class TextureCube(
  gl : WebGL2RenderingContext,
  vararg mediaFileUrls : String
  ) : Texture {
  override val glTexture : WebGLTexture? = gl.createTexture()
  init {
    val images = Array<Image>(6) { Image() }
    var loadedCount = 0    
    for(i in 0 until 6) {
      images[i].onload = {
        gl.bindTexture(GL.TEXTURE_CUBE_MAP, glTexture)
        gl.texImage2D(GL.TEXTURE_CUBE_MAP_POSITIVE_X+i, 0, GL.RGBA, GL.RGBA, GL.UNSIGNED_BYTE, images[i]) 
        if(++loadedCount == 6) {
          gl.texParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MAG_FILTER, GL.LINEAR) 
          gl.texParameteri(GL.TEXTURE_CUBE_MAP, GL.TEXTURE_MIN_FILTER, GL.LINEAR_MIPMAP_LINEAR) 
          gl.generateMipmap(GL.TEXTURE_CUBE_MAP); 
        }
        gl.bindTexture(GL.TEXTURE_CUBE_MAP, null); 
      }
      images[i].src = mediaFileUrls[i]
    }
  }
}