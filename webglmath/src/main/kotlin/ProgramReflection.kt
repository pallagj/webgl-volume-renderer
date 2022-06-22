package vision.gears.webglmath

import org.khronos.webgl.WebGLRenderingContext
import org.khronos.webgl.WebGLUniformLocation
import org.khronos.webgl.WebGLProgram

val WebGLRenderingContext.Companion.UNSIGNED_INT_SAMPLER_2D : Int get() = 0x8DD2
val WebGLRenderingContext.Companion.INT_SAMPLER_2D : Int get() = 0x8DCA
val WebGLRenderingContext.Companion.SAMPLER_2D_SHADOW : Int get() = 0x8B62
val WebGLRenderingContext.Companion.UNSIGNED_INT_SAMPLER_CUBE : Int get() = 0x8DD4
val WebGLRenderingContext.Companion.INT_SAMPLER_CUBE : Int get() = 0x8DCC
val WebGLRenderingContext.Companion.SAMPLER_CUBE_SHADOW : Int get() = 0x8DC5
val WebGLRenderingContext.Companion.UNSIGNED_INT_SAMPLER_3D : Int get() = 0x8DD3
val WebGLRenderingContext.Companion.INT_SAMPLER_3D : Int get() = 0x8DCB
val WebGLRenderingContext.Companion.SAMPLER_3D : Int get() = 0x8B5F
val WebGLRenderingContext.Companion.UNSIGNED_INT_SAMPLER_2D_ARRAY : Int get() = 0x8DD7
val WebGLRenderingContext.Companion.INT_SAMPLER_2D_ARRAY : Int get() = 0x8DCF
val WebGLRenderingContext.Companion.SAMPLER_2D_ARRAY_SHADOW : Int get() = 0x8DC4
val WebGLRenderingContext.Companion.SAMPLER_2D_ARRAY : Int get() = 0x8DC1

class UniformDescriptor(val name:String, val type: Int, val size: Int, val location: WebGLUniformLocation){
}

class ProgramReflection(val gl : WebGLRenderingContext, val glProgram : WebGLProgram) : Drawable() {

  val uniformDescriptors = HashMap<String, ArrayList<UniformDescriptor> >()

  init{
  	// for all uniforms used in glProgram
  	val nUniforms = gl.getProgramParameter(glProgram, WebGLRenderingContext.ACTIVE_UNIFORMS) as Int
  	for(i in 0 until nUniforms){ 
  	  val glUniform = gl.getActiveUniform(glProgram, i)!!
  	  // separate struct name (if exists) and unqualified uniform name
  	  val nameParts = glUniform.name.split(".")
  	  val uniformName = nameParts.lastOrNull() ?: continue
  	  val structName = nameParts.dropLast(1).lastOrNull() ?: continue

      uniformDescriptors[structName] = this.uniformDescriptors[structName] ?: ArrayList<UniformDescriptor>()
      uniformDescriptors[structName]!!.add( UniformDescriptor(
        name = uniformName,
        type = glUniform.type,
        size = glUniform.size,
        location = gl.getUniformLocation(glProgram, glUniform.name)!!
      ))
    }
  }
  
  override fun gatherUniforms(target : UniformProvider){
    for(structName in target.glslStructNames) {
      // Skip GLSL struct provided by the target if the program does not need it.
      val descList = uniformDescriptors[structName] ?: continue

      for(uniformDesc in descList) {
        val reflectionVariable = ProgramReflection.makeVar(uniformDesc.type, uniformDesc.size)

        if(target.uniforms.containsKey(uniformDesc.name)){ // if reflection property already exists, check compatibility
          val existingVariable = target.uniforms[uniformDesc.name] ?: throw Error("Uniform is null.")
          if(existingVariable::class != reflectionVariable::class ||
            existingVariable.getStorageSize() != reflectionVariable.getStorageSize()){
            throw Error("Trying to reflect uniform ${uniformDesc.name} as a ${reflectionVariable::class.simpleName} with element count ${reflectionVariable.getStorageSize()}, but it already exists in the target object as a ${(existingVariable::class.simpleName)} with element count ${existingVariable.getStorageSize()}.")
          }
        } else {
          target.uniforms[uniformDesc.name] = reflectionVariable
        }
      }
    }
  }
  
  /**
   * Sets values of all uniforms from the properties of the given objects.
   * @param {...UniformProvider} uniformProviders - Objects with properties matching the names and the types of the uniforms to be set. Their glslStructNames property must list uniform struct names they are responsible for setting.
   */
  override fun draw(vararg uniformProviders : UniformProvider) { 
    gl.useProgram(glProgram)
    var textureUnitCount = 0

    for(provider in uniformProviders){
      for(structName in provider.glslStructNames) {
        val descList = uniformDescriptors[structName] ?: continue
        for(uniformDesc in descList) {
          provider[uniformDesc.name]!!.commit(gl, uniformDesc.location, textureUnitCount)
          //  keep track of texture units used
          if( ProgramReflection.isSampler(uniformDesc.type) ){ 
            textureUnitCount += uniformDesc.size
          }
        }
      }
    }
  }

  companion object {
    /**
     * @method makeVar
     * @memberof ProgramReflection
     * @static 
     * @description Returns a new reflection variable based on a numerical WebGL type ID.
     * @param {Number} type - The numeric type of the uniform, i.e. a value of a type identifier property in the rendering context.
     * @param {Number} arraySize - The number of elements in the uniform, if it is an array. Otherwise, it must be 1.
     * @return {Vec1 | Vec1Array | Vec2 | Vec2Array | Vec3 | Vec3Array | Vec4 | Vec4Array | Mat4 | Mat4Array | Sampler2D | Sampler2DArray | SamplerCube | SamplerCubeArray | Sampler3D | Sampler3DArray | Sampler2DArrayTexture | Sampler2DArrayTextureArray} The new reflection object.
     */  
    fun makeVar(type : Int, arraySize : Int) : Uniform {
      if(arraySize == 1) {
        when(type) {
          WebGLRenderingContext.FLOAT ->         return Vec1()
          WebGLRenderingContext.FLOAT_VEC2 ->    return Vec2()
          WebGLRenderingContext.FLOAT_VEC3 ->    return Vec3()
          WebGLRenderingContext.FLOAT_VEC4 ->    return Vec4()
          WebGLRenderingContext.FLOAT_MAT4 ->    return Mat4()
          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D , 
//          WebGLRenderingContext.INT_SAMPLER_2D ,
//          WebGLRenderingContext.SAMPLER_2D_SHADOW ,
          WebGLRenderingContext.SAMPLER_2D ->    return Sampler2D()
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_CUBE ,
//          WebGLRenderingContext.INT_SAMPLER_CUBE ,
//          WebGLRenderingContext.SAMPLER_CUBE_SHADOW ,
          WebGLRenderingContext.SAMPLER_CUBE ->  return SamplerCube()
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_3D ,
//          WebGLRenderingContext.INT_SAMPLER_3D ,
          WebGLRenderingContext.SAMPLER_3D ->    return Sampler3D()
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D_ARRAY ,
//          WebGLRenderingContext.INT_SAMPLER_2D_ARRAY ,
//          WebGLRenderingContext.SAMPLER_2D_SHADOW_ARRAY ,
//          WebGLRenderingContext.SAMPLER_2D_ARRAY ->    return Sampler2DArrayTexture()
        }
      } else {
        when(type) {
          WebGLRenderingContext.FLOAT ->         return Vec1Array(arraySize)
          WebGLRenderingContext.FLOAT_VEC2 ->    return Vec2Array(arraySize)
          WebGLRenderingContext.FLOAT_VEC3 ->    return Vec3Array(arraySize)
          WebGLRenderingContext.FLOAT_VEC4 ->    return Vec4Array(arraySize)
          WebGLRenderingContext.FLOAT_MAT4 ->    return Mat4Array(arraySize)
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D ,
//          WebGLRenderingContext.INT_SAMPLER_2D ,
//          WebGLRenderingContext.SAMPLER_2D_SHADOW ,
//          WebGLRenderingContext.SAMPLER_2D ->    return Sampler2DArray(arraySize)
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_CUBE ,
//          WebGLRenderingContext.INT_SAMPLER_CUBE ,
//          WebGLRenderingContext.SAMPLER_CUBE_SHADOW ,
//          WebGLRenderingContext.SAMPLER_CUBE ->  return SamplerCubeArray(arraySize)
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_3D , 
//          WebGLRenderingContext.INT_SAMPLER_3D ,
//          WebGLRenderingContext.SAMPLER_3D ->    return Sampler3DArray(arraySize)
//          WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D_ARRAY ,
//          WebGLRenderingContext.INT_SAMPLER_2D_ARRAY ,
//          WebGLRenderingContext.SAMPLER_2D_ARRAY_SHADOW ,
//          WebGLRenderingContext.SAMPLER_2D_ARRAY ->    return Sampler2DArrayTextureArray(arraySize)
        }
      }
      throw Error("Unhandled uniform variable of type ID ${type}.")
    }


    /**
     * @method isSampler
     * @memberof ProgramReflection
     * @static 
     * @description Returns true if type is a numerical WebGL type ID of a sampler uniform.
     * @param {WebGL2RenderingContext} gl - The rendering context.
     * @param {Number} type - The numeric type of the uniform, i.e. a value of a type identifier property in the rendering context.
     */
    fun isSampler(type : Int) : Boolean {
      return  type == WebGLRenderingContext.SAMPLER_2D ||
              type == WebGLRenderingContext.SAMPLER_3D ||
              type == WebGLRenderingContext.SAMPLER_CUBE ||
              type == WebGLRenderingContext.SAMPLER_2D_SHADOW ||
              type == WebGLRenderingContext.SAMPLER_2D_ARRAY ||
              type == WebGLRenderingContext.SAMPLER_2D_ARRAY_SHADOW ||
              type == WebGLRenderingContext.SAMPLER_CUBE_SHADOW ||
              type == WebGLRenderingContext.INT_SAMPLER_2D ||
              type == WebGLRenderingContext.INT_SAMPLER_3D ||
              type == WebGLRenderingContext.INT_SAMPLER_CUBE ||
              type == WebGLRenderingContext.INT_SAMPLER_2D_ARRAY ||
              type == WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D ||
              type == WebGLRenderingContext.UNSIGNED_INT_SAMPLER_3D ||
              type == WebGLRenderingContext.UNSIGNED_INT_SAMPLER_CUBE ||
              type == WebGLRenderingContext.UNSIGNED_INT_SAMPLER_2D_ARRAY
    }
  }
}