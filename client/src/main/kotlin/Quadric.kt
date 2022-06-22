import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.*

class Quadric(i : Int) : UniformProvider("""quadrics[${i}]""") {
  val surface by QuadraticMat4(unitSphere.clone())
  val clipper by QuadraticMat4(unitSlab.clone())  
  val reflectance by Vec3(1f, 1f, 1f)

  companion object {
    val unitSphere = 
      Mat4(
        1.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 1.0f, 0.0f,
        0.0f, 0.0f, 0.0f, -1.0f
      )
    val unitSlab = 
      Mat4(
        0.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 1.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, 0.0f,
        0.0f, 0.0f, 0.0f, -1.0f
      )      
  }

}