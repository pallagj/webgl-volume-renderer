package vision.gears.webglmath

abstract class Drawable {
  open fun gatherUniforms(target : UniformProvider){}
  abstract fun draw(vararg uniformProviders : UniformProvider)
  open fun drawWithOverrides(overrides : Map<String, UniformProvider>, vararg uniformProviders : UniformProvider){
    draw(*uniformProviders)
  }
}
