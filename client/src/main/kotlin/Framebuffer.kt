import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.WebGLTexture

class Framebuffer(
  gl : WebGL2RenderingContext,
  targetCount : Int = 1,
  val width : Int = 512,
  val height : Int = 512,
  internalFormat : Int = GL.RGBA,
  format : Int = internalFormat,
  type : Int = GL.UNSIGNED_BYTE  
  ) {

  val glFramebuffer = gl.createFramebuffer()
  val targets = Array<RenderTargetTexture>(targetCount) { 
    RenderTargetTexture(gl, width, height, internalFormat, format, type)
  }
  val depthBuffer = gl.createRenderbuffer()
  init {
    gl.bindFramebuffer(GL.FRAMEBUFFER, glFramebuffer)
    gl.drawBuffers(IntArray(targetCount){GL.COLOR_ATTACHMENT0 + it})    
    targets.forEachIndexed { i, target ->
      gl.framebufferTexture2D(GL.FRAMEBUFFER, GL.COLOR_ATTACHMENT0 + i,
       GL.TEXTURE_2D, target.glTexture, 0)
    }
    gl.bindRenderbuffer(GL.RENDERBUFFER, depthBuffer)
    gl.renderbufferStorage(GL.RENDERBUFFER, GL.DEPTH_COMPONENT16, width, height)
    gl.framebufferRenderbuffer(GL.FRAMEBUFFER, GL.DEPTH_ATTACHMENT, GL.RENDERBUFFER, depthBuffer)
    gl.bindFramebuffer(GL.FRAMEBUFFER, null)
  }

  fun bind(gl : WebGL2RenderingContext){
    gl.bindFramebuffer(GL.FRAMEBUFFER, glFramebuffer)
    gl.viewport(0, 0, width, height)
  }
}
