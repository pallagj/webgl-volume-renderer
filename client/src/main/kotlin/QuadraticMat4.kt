import vision.gears.webglmath.UniformFloat
import vision.gears.webglmath.UniformProvider
import vision.gears.webglmath.Mat4
import kotlin.reflect.KProperty

class QuadraticMat4(val m : Mat4 = Mat4()) : UniformFloat by m {

  fun transform(trafo : Mat4){
  	m.set( 
  		trafo.clone().invert() *
  		m *
  		trafo.clone().invert().transpose() ) 
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : QuadraticMat4{
    provider.register(property.name, this)
    return this
  }  

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): QuadraticMat4 {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: QuadraticMat4) {
    set(value)
  }  


}
