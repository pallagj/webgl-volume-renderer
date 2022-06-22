import org.khronos.webgl.WebGLRenderingContext as GL
import org.khronos.webgl.Float32Array
import org.khronos.webgl.Uint16Array
import vision.gears.webglmath.Geometry

class TexturedQuadGeometry(val gl : WebGL2RenderingContext) : Geometry() {
   
  val vertexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( arrayOf<Float>(
        -1.0f, -1.0f, 0.5f,
        -1.0f,  1.0f, 0.5f,
         1.0f, -1.0f, 0.5f,
         1.0f,  1.0f, 0.5f
      )),
      GL.STATIC_DRAW)
  }

  val vertexNormalBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexNormalBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( arrayOf<Float>(
         0.0f,  0.0f, 1.0f,
         0.0f,  0.0f, 1.0f,
         0.0f,  0.0f, 1.0f,
         0.0f,  0.0f, 1.0f
      )),
      GL.STATIC_DRAW)
  }

  val vertexTexCoordBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexTexCoordBuffer)
    gl.bufferData(GL.ARRAY_BUFFER,
      Float32Array( arrayOf<Float>(
         0.0f,  1.0f, 
         0.0f,  0.0f, 
         1.0f,  1.0f, 
         1.0f,  0.0f
         )),
      GL.STATIC_DRAW)
  }    

  val indexBuffer = gl.createBuffer()
  init{
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)
    gl.bufferData(GL.ELEMENT_ARRAY_BUFFER,
      Uint16Array( arrayOf<Short>(
        0, 1, 2,
        1, 2, 3
      )),
      GL.STATIC_DRAW)
  }

  val inputLayout = gl.createVertexArray()
  init{
    gl.bindVertexArray(inputLayout)

    gl.bindBuffer(GL.ARRAY_BUFFER, vertexBuffer)
    gl.enableVertexAttribArray(0)
    gl.vertexAttribPointer(0,
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexNormalBuffer)
    gl.enableVertexAttribArray(1)
    gl.vertexAttribPointer(1,
      3, GL.FLOAT, //< three pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )
    gl.bindBuffer(GL.ARRAY_BUFFER, vertexTexCoordBuffer)
    gl.enableVertexAttribArray(2)
    gl.vertexAttribPointer(2,
      2, GL.FLOAT, //< two pieces of float
      false, //< do not normalize (make unit length)
      0, //< tightly packed
      0 //< data starts at array start
    )    
    gl.bindVertexArray(null)
  }

  override fun draw() {
    gl.bindVertexArray(inputLayout)
    gl.bindBuffer(GL.ELEMENT_ARRAY_BUFFER, indexBuffer)  

    gl.drawElements(GL.TRIANGLES, 6, GL.UNSIGNED_SHORT, 0)
  }
}
