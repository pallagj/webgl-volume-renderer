import vision.gears.webglmath.*

class Light(i : Int) : UniformProvider("""lights[${i}]""") {
  val position by Vec4()
  val powerDensity by Vec4()
}