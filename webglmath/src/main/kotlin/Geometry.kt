package vision.gears.webglmath

abstract class Geometry : Drawable() {
	abstract fun draw()

  final override fun draw(vararg uniformProviders : UniformProvider){
  	draw()
  }

  // This implementation is identical to Drawable::drawWithOverrides. Is there a way to make the method final withot overriding it?
  final override fun drawWithOverrides(overrides : Map<String, UniformProvider>, vararg uniformProviders : UniformProvider) {
  	draw(*uniformProviders)
	}
  // This empty implementation is identical to Drawable::drawWithOverrides. Is there a way to make the method final withot overriding it?
  final override fun gatherUniforms(target : UniformProvider){}
}
