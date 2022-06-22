import org.khronos.webgl.WebGLRenderingContext as GL

class DefaultFramebuffer(
  val width : Int,
  val height : Int
  ){
  fun bind(gl : WebGL2RenderingContext){
    gl.bindFramebuffer(GL.FRAMEBUFFER, null)
    gl.viewport(0, 0, width, height)
  }
}