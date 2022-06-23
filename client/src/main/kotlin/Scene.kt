import org.w3c.dom.HTMLCanvasElement
import vision.gears.webglmath.*
import org.khronos.webgl.WebGLRenderingContext as GL
import kotlin.js.Date

class Scene (
  val gl : WebGL2RenderingContext) : UniformProvider("scene"){

  val vsQuad = Shader(gl, GL.VERTEX_SHADER, "quad-vs.glsl")
  val fsTrace = Shader(gl, GL.FRAGMENT_SHADER, "trace-fs.glsl")  
  val traceProgram = Program(gl, vsQuad, fsTrace, Program.PNT)
  val quadGeometry = TexturedQuadGeometry(gl)  

  val timeAtFirstFrame = Date().getTime()
  var timeAtLastFrame =  timeAtFirstFrame

  val camera = PerspectiveCamera(*Program.all)

  val lights = ArrayList<Light>()

  val env by SamplerCube()

  val volumeTexture = Texture3D(gl, "media/brain-at_4096.jpg")
  val matcapTexture1 = Texture2D(gl, "media/matcap1.jpg")
  val matcapTexture2 = Texture2D(gl, "media/matcap0.jpg")
  val volume = Sampler3D()
  val matcap1 = Sampler2D()
  val matcap2 = Sampler2D()
  var state by Vec1(0.0f)
  var layerWidth by Vec1(0.01f)


  init {
    env.set(TextureCube(gl, "media/posx512.jpg", "media/negx512.jpg"
      , "media/posy512.jpg", "media/negy512.jpg", "media/posz512.jpg", "media/negz512.jpg"))
    addComponentsAndGatherUniforms(*Program.all)
    lights.add(Light(0))
    lights[0].position.set(-3.0f, -2.0f, -3.1f, 0f)//(1f, 1f, 1f, 0f).normalize()
    lights[0].powerDensity.set(10.951f, 10.951f, 10.951f, 1f)

    lights.add(Light(1))
    lights[1].position.set(3.5f, -3.0f, 3.0f, 1.0f)//2.5f, -2.0f, 0f, 1f
    lights[1].powerDensity.set(13f, 13f, 13f, 1f)

    register("volume", volume)
    register("matcap1", matcap1)
    register("matcap2", matcap2)
    volume.glTextures[0] = volumeTexture.glTexture
    matcap1.glTextures[0] = matcapTexture1.glTexture
    matcap2.glTextures[0] = matcapTexture2.glTexture
    addComponentsAndGatherUniforms(*Program.all)

  }

  fun resize(gl : WebGL2RenderingContext, canvas : HTMLCanvasElement) {
    gl.viewport(0, 0, canvas.width, canvas.height)
    camera.setAspectRatio(canvas.width.toFloat() / canvas.height.toFloat())
  }

  @Suppress("UNUSED_PARAMETER")
  fun update(gl : WebGL2RenderingContext, keysPressed : Set<String>) {

    val timeAtThisFrame = Date().getTime() 
    val dt = (timeAtThisFrame - timeAtLastFrame).toFloat() / 1000.0f
    val t  = (timeAtThisFrame - timeAtFirstFrame).toFloat() / 1000.0f    
    timeAtLastFrame = timeAtThisFrame
    
    camera.move(dt, keysPressed)

    if("0" in keysPressed){
     state = Vec1(0.0f)
    }
    if("1" in keysPressed){
      state = Vec1(1.0f)
    }
    if("2" in keysPressed){
      state = Vec1(2.0f)
    }
    if("3" in keysPressed){
      state = Vec1(3.0f)
    }

    if("M" in keysPressed){
      layerWidth = Vec1(layerWidth.x + 0.001f)
    }

    if("N" in keysPressed){
      layerWidth = Vec1(layerWidth.x  - 0.001f)
    }
    // clear the screen
    gl.clearColor(0.0f, 0.0f, 0.3f, 1.0f)
    gl.clearDepth(1.0f)
    gl.clear(GL.COLOR_BUFFER_BIT or GL.DEPTH_BUFFER_BIT)
    
    traceProgram.draw(this, *lights.toTypedArray(), camera)
    quadGeometry.draw()    
  }
}
