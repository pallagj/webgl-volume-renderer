import org.w3c.xhr.XMLHttpRequest
import org.w3c.dom.events.*
import org.w3c.dom.HTMLScriptElement
import org.khronos.webgl.WebGLRenderingContext as GL

class Shader (
  val gl : WebGL2RenderingContext,
  val shaderType : Int,
  val sourceUrl : String) {

  val glShader = gl.createShader(shaderType) //#createShader# get an OpenGL shader ID #shaderType# vertex or fragment shader

  init {
    val request = XMLHttpRequest()
    request.overrideMimeType("text/plain")
    request.open("GET", sourceUrl, false)
    var errorMessage : String? = null
    request.onloadend = {
      val source = request.responseText
      if(source == ""){
        errorMessage = "${sourceUrl} is empty."
      } else {
        val marked = Regex("[^" + '\u0000' + "-" + '\u007F'+ "]").replace(source) {
          match -> '\u001b' + "[46m" + match.value + '\u001b' + "[31m" }
        if(marked != source) {
          errorMessage = "Shader source file ${sourceUrl} has \u001b[46mnon-ASCII\u001b[31m characters.\n" + marked
        } else {
          gl.shaderSource(glShader, source) //#shaderSource# associate source code with ID
          gl.compileShader(glShader) //#compileShader# compile GLSL code to binary
          if (gl.getShaderParameter(glShader, GL.COMPILE_STATUS) == false) {
            val errorPrefix = Regex("ERROR: 0")
            errorMessage = "Shader ${sourceUrl} had compilation errors.\n${errorPrefix.replace(gl.getShaderInfoLog(glShader)?:"FAILED TO OBTAIN LOG.", request.responseURL)}" //#getShaderInfoLog# check if succeeded and get compilation error message
          }
        }
      }
    } 
    request.send()
    if(errorMessage != null){
        throw Error(errorMessage)
    }
  }
}
