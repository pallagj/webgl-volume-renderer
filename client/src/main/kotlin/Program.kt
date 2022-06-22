import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.ProgramReflection

class Program(
  val gl : WebGL2RenderingContext,
  val vertexShader : Shader,
  val fragmentShader : Shader,
  attributeBindings : Array<String> = Program.PNT ) : UniformProvider("program") {

  val glProgram = gl.createProgram() ?: throw Error("Could not create WebGL program.")

  init {
    gl.attachShader(glProgram, vertexShader.glShader)
    gl.attachShader(glProgram, fragmentShader.glShader)

    var attributeIndex = 0
    attributeBindings.forEach{
      gl.bindAttribLocation(glProgram, attributeIndex++, it)  
    }    

    gl.linkProgram(glProgram)
    if (gl.getProgramParameter(glProgram, GL.LINK_STATUS) == false) {
      throw Error("Could not link shaders [vertex shader: ${vertexShader.sourceUrl}]:[fragment shader: ${fragmentShader.sourceUrl}\n${gl.getProgramInfoLog(this.glProgram)}")
    }

    addComponentsAndGatherUniforms(ProgramReflection(gl, glProgram))
    Program.all += this
  }

  companion object{
    var all = emptyArray<Program>()
    val PC = arrayOf("vertexPosition", "vertexColor")
    val PNT = arrayOf("vertexPosition", "vertexNormal", "vertexTexCoord")
  }
}