package vision.gears.webglmath

import org.khronos.webgl.Float32Array
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import kotlin.reflect.KProperty
import kotlin.math.sqrt
import kotlin.math.cos
import kotlin.math.sin

class Mat4 (backingStorage: Float32Array?, offset: Int = 0) : UniformFloat {

  constructor(
//    m00 : Float = 1.0f, m01: Float = 0.0f, m02: Float = 0.0f, m03: Float = 0.0f,
//    m10 : Float = 0.0f, m11: Float = 1.0f, m12: Float = 0.0f, m13: Float = 0.0f,
//    m20 : Float = 0.0f, m21: Float = 0.0f, m22: Float = 1.0f, m23: Float = 0.0f,
//    m30 : Float = 0.0f, m31: Float = 0.0f, m32: Float = 0.0f, m33: Float = 1.0f) : this(null, 0){
    vararg elements : Float ) : this(null, 0){
    val allElements = Array<Float>(16) {
      i : Int ->
      elements.getOrNull((i % 4)*4 + i/4)?: if(i%5==0) 1.0f else 0.0f
    }
    storage.set(allElements)
  }
  constructor(other : Mat4) : this(null, 0)  {
    storage.set(other.storage)
  }

  override val storage: Float32Array = backingStorage?.subarray(offset, offset+16)?:Float32Array(16)

  fun clone() : Mat4 {
    return Mat4(this)
  }

  override fun set(vararg values : Float ) : Mat4 {
    val allElements = Array<Float>(16) {
      i : Int ->
      values.getOrNull((i % 4)*4 + i/4)?: if(i%5==0) 1.0f else 0.0f
    }
    storage.set(allElements)
    return this
  }

  override fun set(other : Uniform) : Mat4{
    if(other is Mat4) {
      storage.set(other.storage)
    } else {
      throw Error("A Mat4 cannot be set from a Uniform of another type.")
    }
    return this
  }  

  fun premul(m : Mat4) : Mat4 {
    val n00 = storage[0]
    val n01 = storage[4]
    val n02 = storage[8]
    val n03 = storage[12]
    val n10 = storage[1]
    val n11 = storage[5]
    val n12 = storage[9]
    val n13 = storage[13]
    val n20 = storage[2]
    val n21 = storage[6]
    val n22 = storage[10]
    val n23 = storage[14]
    val n30 = storage[3]
    val n31 = storage[7]
    val n32 = storage[11]
    val n33 = storage[15]
    val m00 = m.storage[0]
    val m01 = m.storage[4]
    val m02 = m.storage[8]
    val m03 = m.storage[12]
    val m10 = m.storage[1]
    val m11 = m.storage[5]
    val m12 = m.storage[9]
    val m13 = m.storage[13]
    val m20 = m.storage[2]
    val m21 = m.storage[6]
    val m22 = m.storage[10]
    val m23 = m.storage[14]
    val m30 = m.storage[3]
    val m31 = m.storage[7]
    val m32 = m.storage[11]
    val m33 = m.storage[15]
    storage[0] = (m00 * n00) + (m01 * n10) + (m02 * n20) + (m03 * n30)
    storage[1] = (m10 * n00) + (m11 * n10) + (m12 * n20) + (m13 * n30)
    storage[2] = (m20 * n00) + (m21 * n10) + (m22 * n20) + (m23 * n30)
    storage[3] = (m30 * n00) + (m31 * n10) + (m32 * n20) + (m33 * n30)
    storage[4] = (m00 * n01) + (m01 * n11) + (m02 * n21) + (m03 * n31)
    storage[5] = (m10 * n01) + (m11 * n11) + (m12 * n21) + (m13 * n31)
    storage[6] = (m20 * n01) + (m21 * n11) + (m22 * n21) + (m23 * n31)
    storage[7] = (m30 * n01) + (m31 * n11) + (m32 * n21) + (m33 * n31)
    storage[8] = (m00 * n02) + (m01 * n12) + (m02 * n22) + (m03 * n32)
    storage[9] = (m10 * n02) + (m11 * n12) + (m12 * n22) + (m13 * n32)
    storage[10] = (m20 * n02) + (m21 * n12) + (m22 * n22) + (m23 * n32)
    storage[11] = (m30 * n02) + (m31 * n12) + (m32 * n22) + (m33 * n32)
    storage[12] = (m00 * n03) + (m01 * n13) + (m02 * n23) + (m03 * n33)
    storage[13] = (m10 * n03) + (m11 * n13) + (m12 * n23) + (m13 * n33)
    storage[14] = (m20 * n03) + (m21 * n13) + (m22 * n23) + (m23 * n33)
    storage[15] = (m30 * n03) + (m31 * n13) + (m32 * n23) + (m33 * n33)
    return this
  }

  operator fun timesAssign(m : Mat4){
    val m00 = storage[0]
    val m01 = storage[4]
    val m02 = storage[8]
    val m03 = storage[12]
    val m10 = storage[1]
    val m11 = storage[5]
    val m12 = storage[9]
    val m13 = storage[13]
    val m20 = storage[2]
    val m21 = storage[6]
    val m22 = storage[10]
    val m23 = storage[14]
    val m30 = storage[3]
    val m31 = storage[7]
    val m32 = storage[11]
    val m33 = storage[15]
    val n00 = m.storage[0]
    val n01 = m.storage[4]
    val n02 = m.storage[8]
    val n03 = m.storage[12]
    val n10 = m.storage[1]
    val n11 = m.storage[5]
    val n12 = m.storage[9]
    val n13 = m.storage[13]
    val n20 = m.storage[2]
    val n21 = m.storage[6]
    val n22 = m.storage[10]
    val n23 = m.storage[14]
    val n30 = m.storage[3]
    val n31 = m.storage[7]
    val n32 = m.storage[11]
    val n33 = m.storage[15]
    storage[0] = (m00 * n00) + (m01 * n10) + (m02 * n20) + (m03 * n30)
    storage[1] = (m10 * n00) + (m11 * n10) + (m12 * n20) + (m13 * n30)
    storage[2] = (m20 * n00) + (m21 * n10) + (m22 * n20) + (m23 * n30)
    storage[3] = (m30 * n00) + (m31 * n10) + (m32 * n20) + (m33 * n30)
    storage[4] = (m00 * n01) + (m01 * n11) + (m02 * n21) + (m03 * n31)
    storage[5] = (m10 * n01) + (m11 * n11) + (m12 * n21) + (m13 * n31)
    storage[6] = (m20 * n01) + (m21 * n11) + (m22 * n21) + (m23 * n31)
    storage[7] = (m30 * n01) + (m31 * n11) + (m32 * n21) + (m33 * n31)
    storage[8] = (m00 * n02) + (m01 * n12) + (m02 * n22) + (m03 * n32)
    storage[9] = (m10 * n02) + (m11 * n12) + (m12 * n22) + (m13 * n32)
    storage[10] = (m20 * n02) + (m21 * n12) + (m22 * n22) + (m23 * n32)
    storage[11] = (m30 * n02) + (m31 * n12) + (m32 * n22) + (m33 * n32)
    storage[12] = (m00 * n03) + (m01 * n13) + (m02 * n23) + (m03 * n33)
    storage[13] = (m10 * n03) + (m11 * n13) + (m12 * n23) + (m13 * n33)
    storage[14] = (m20 * n03) + (m21 * n13) + (m22 * n23) + (m23 * n33)
    storage[15] = (m30 * n03) + (m31 * n13) + (m32 * n23) + (m33 * n33)
  }

  operator fun times(m : Mat4) : Mat4 {
    val res = Mat4(this)
    res *= m
    return res
  }

  operator fun timesAssign(s : Float) {
    storage[ 0] *= s
    storage[ 1] *= s
    storage[ 2] *= s
    storage[ 3] *= s
    storage[ 4] *= s
    storage[ 5] *= s
    storage[ 6] *= s
    storage[ 7] *= s
    storage[ 8] *= s
    storage[ 9] *= s
    storage[10] *= s
    storage[12] *= s
    storage[13] *= s
    storage[14] *= s
    storage[15] *= s
  }

  operator fun times(s : Float) : Mat4 {
    val res = Mat4(this)
    res *= s
    return res    
  }

  operator fun times(v : Vec4) : Vec4 {
    val vp = Vec4()
    vp.storage[0] = storage[ 0] * v.storage[0] + storage[ 1] * v.storage[1] + storage[ 2] * v.storage[2] + storage[ 3] * v.storage[3]
    vp.storage[1] = storage[ 4] * v.storage[0] + storage[ 5] * v.storage[1] + storage[ 6] * v.storage[2] + storage[ 7] * v.storage[3]
    vp.storage[2] = storage[ 8] * v.storage[0] + storage[ 9] * v.storage[1] + storage[10] * v.storage[2] + storage[11] * v.storage[3]
    vp.storage[3] = storage[12] * v.storage[0] + storage[13] * v.storage[1] + storage[14] * v.storage[2] + storage[15] * v.storage[3]        
    return vp    
  }

  fun scale(s : Vec2) : Mat4 { return scale(s.storage[0], s.storage[1], 1.0f)}
  fun scale(s : Vec3) : Mat4 { return scale(s.storage[0], s.storage[1], s.storage[2])}  
  fun scale(sx : Float = 1.0f, sy : Float = 1.0f, sz : Float = 1.0f) : Mat4 {
    storage[ 0] *= sx
    storage[ 1] *= sx
    storage[ 2] *= sx
    storage[ 3] *= sx
    storage[ 4] *= sy
    storage[ 5] *= sy
    storage[ 6] *= sy
    storage[ 7] *= sy
    storage[ 8] *= sz
    storage[ 9] *= sz
    storage[10] *= sz
    storage[11] *= sz
    return this  
  }

  fun rotate(angle : Float, axis : Vec3) : Mat4 { return rotate(angle, axis.storage[0], axis.storage[1], axis.storage[2])}
  fun rotate(angle : Float = 0.0f, axisX : Float = 0.0f, axisY : Float = 0.0f, axisZ : Float = 0.0f) : Mat4 {
    var x = axisX
    var y = axisY
    var z = axisZ        
    val axisLength2 = x*x + y*y + z*z
    if(axisLength2 < 0.0001f){
      x=0.0f
      y=0.0f
      z=1.0f
    } else if(axisLength2 < 0.999f || axisLength2 > 1.001f) {
      val axisLength = sqrt(axisLength2)
      x /= axisLength
      y /= axisLength
      z /= axisLength
    }
    val cosa = cos(angle)
    val sina = sin(angle)
    val C = 1.0f - cosa
    val m11 = x * x * C + cosa
    val m21 = x * y * C - z * sina
    val m31 = x * z * C + y * sina
    val m12 = y * x * C + z * sina
    val m22 = y * y * C + cosa
    val m32 = y * z * C - x * sina
    val m13 = z * x * C - y * sina
    val m23 = z * y * C + x * sina
    val m33 = z * z * C + cosa
    val t0  = storage[ 0] * m11 + storage[ 4] * m21 + storage[ 8] * m31
    val t4  = storage[ 0] * m12 + storage[ 4] * m22 + storage[ 8] * m32
    val t8  = storage[ 0] * m13 + storage[ 4] * m23 + storage[ 8] * m33
    val t1  = storage[ 1] * m11 + storage[ 5] * m21 + storage[ 9] * m31
    val t5  = storage[ 1] * m12 + storage[ 5] * m22 + storage[ 9] * m32
    val t9  = storage[ 1] * m13 + storage[ 5] * m23 + storage[ 9] * m33
    val t2  = storage[ 2] * m11 + storage[ 6] * m21 + storage[10] * m31
    val t6  = storage[ 2] * m12 + storage[ 6] * m22 + storage[10] * m32
    val t10 = storage[ 2] * m13 + storage[ 6] * m23 + storage[10] * m33
    val t3  = storage[ 3] * m11 + storage[ 7] * m21 + storage[11] * m31
    val t7  = storage[ 3] * m12 + storage[ 7] * m22 + storage[11] * m32
    val t11 = storage[ 3] * m13 + storage[ 7] * m23 + storage[11] * m33
    storage[ 0] = t0 
    storage[ 4] = t4 
    storage[ 8] = t8 
    storage[ 1] = t1 
    storage[ 5] = t5 
    storage[ 9] = t9 
    storage[ 2] = t2 
    storage[ 6] = t6 
    storage[10] = t10
    storage[ 3] = t3 
    storage[ 7] = t7 
    storage[11] = t11
    return this  
  }

  fun translate(t : Vec2) : Mat4 { return translate(t.storage[0], t.storage[1])}  
  fun translate(t : Vec3) : Mat4 { return translate(t.storage[0], t.storage[1], t.storage[2])}  
  fun translate(x : Float = 0.0f, y : Float = 0.0f, z : Float = 0.0f) : Mat4 {
    storage[ 0] += storage[12] * x
    storage[ 4] += storage[12] * y
    storage[ 8] += storage[12] * z
    storage[ 1] += storage[13] * x
    storage[ 5] += storage[13] * y
    storage[ 9] += storage[13] * z
    storage[ 2] += storage[14] * x
    storage[ 6] += storage[14] * y
    storage[10] += storage[14] * z  
    storage[ 3] += storage[15] * x
    storage[ 7] += storage[15] * y
    storage[11] += storage[15] * z
    return this      
  }

  fun transpose() : Mat4 {
    var temp = storage[4]
    storage[4] = storage[1]
    storage[1] = temp
    temp = storage[8]
    storage[8] = storage[2]
    storage[2] = temp
    temp = storage[12]
    storage[12] = storage[3]
    storage[3] = temp
    temp = storage[9]
    storage[9] = storage[6]
    storage[6] = temp
    temp = storage[13]
    storage[13] = storage[7]
    storage[7] = temp
    temp = storage[14]
    storage[14] = storage[11]
    storage[11] = temp
    return this  
  }

  fun invert() : Mat4 {
    val a00 = storage[0]
    val a01 = storage[1]
    val a02 = storage[2]
    val a03 = storage[3]
    val m000 = storage[4]
    val m001 = storage[5]
    val m002 = storage[6]
    val m003 = storage[7]
    val m100 = storage[8]
    val m101 = storage[9]
    val m102 = storage[10]
    val m103 = storage[11]
    val m200 = storage[12]
    val m201 = storage[13]
    val m202 = storage[14]
    val m203 = storage[15]
    val b00 = a00 * m001 - a01 * m000
    val b01 = a00 * m002 - a02 * m000
    val b02 = a00 * m003 - a03 * m000
    val b03 = a01 * m002 - a02 * m001
    val b04 = a01 * m003 - a03 * m001
    val b05 = a02 * m003 - a03 * m002
    val b06 = m100 * m201 - m101 * m200
    val b07 = m100 * m202 - m102 * m200
    val b08 = m100 * m203 - m103 * m200
    val b09 = m101 * m202 - m102 * m201
    val m010 = m101 * m203 - m103 * m201
    val m011 = m102 * m203 - m103 * m202
    val det =
        (b00 * m011 - b01 * m010 + b02 * b09 + b03 * b08 - b04 * b07 + b05 * b06)
    if (det == 0.0f) {
        return this
    }
    val invDet = 1.0f / det
    storage[0] = (m001 * m011 - m002 * m010 + m003 * b09) * invDet
    storage[1] = (-a01 * m011 + a02 * m010 - a03 * b09) * invDet
    storage[2] = (m201 * b05 - m202 * b04 + m203 * b03) * invDet
    storage[3] = (-m101 * b05 + m102 * b04 - m103 * b03) * invDet
    storage[4] = (-m000 * m011 + m002 * b08 - m003 * b07) * invDet
    storage[5] = (a00 * m011 - a02 * b08 + a03 * b07) * invDet
    storage[6] = (-m200 * b05 + m202 * b02 - m203 * b01) * invDet
    storage[7] = (m100 * b05 - m102 * b02 + m103 * b01) * invDet
    storage[8] = (m000 * m010 - m001 * b08 + m003 * b06) * invDet
    storage[9] = (-a00 * m010 + a01 * b08 - a03 * b06) * invDet
    storage[10] = (m200 * b04 - m201 * b02 + m203 * b00) * invDet
    storage[11] = (-m100 * b04 + m101 * b02 - m103 * b00) * invDet
    storage[12] = (-m000 * b09 + m001 * b07 - m002 * b06) * invDet
    storage[13] = (a00 * b09 - a01 * b07 + a02 * b06) * invDet
    storage[14] = (-m200 * b03 + m201 * b01 - m202 * b00) * invDet
    storage[15] = (m100 * b03 - m101 * b01 + m102 * b00) * invDet
    return this
  }

  operator fun provideDelegate(
      provider: UniformProvider,
      property: KProperty<*>) : Mat4{
    provider.register(property.name, this)
    return this
  }

  operator fun getValue(provider: UniformProvider, property: KProperty<*>): Mat4 {
    return this
  }

  operator fun setValue(provider: UniformProvider, property: KProperty<*>, value: Mat4) {
    set(value)
  }  


  override fun commit(gl : WebGLRenderingContext, uniformLocation : WebGLUniformLocation, samplerIndex : Int){
    gl.uniformMatrix4fv(uniformLocation, false, storage);
  }

}
