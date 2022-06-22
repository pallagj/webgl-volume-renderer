import org.khronos.webgl.WebGLRenderingContext as GL
import org.w3c.dom.ImageBitmap
import org.khronos.webgl.Uint8Array

abstract class VertexArray {}

abstract external class WebGL2RenderingContext : GL {

  fun drawBuffers(buffers: IntArray)
  fun drawElementsInstanced(mode: Int, count: Int, type: Int, offset: Int, instanceCount: Int)
  fun readBuffer(src: Int)
  fun renderbufferStorageMultisample(target: Int, samples: Int, internalformat: Int, width: Int, height: Int)
  fun vertexAttribDivisor(index: Int, divisor: Int)
  fun vertexAttribIPointer(index: Int, size: Int, type: Int, stride: Int, offset: Int)
  fun createVertexArray() : VertexArray
  fun bindVertexArray(vertexArray: VertexArray?)
  fun texImage3D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, depth: Int, border: Int, format: Int, type: Int, source: ImageBitmap)
  fun texImage3D(target: Int, level: Int, internalformat: Int, width: Int, height: Int, depth: Int, border: Int, format: Int, type: Int, source: Uint8Array)  
}

val GL.Companion.RED: Int
	get() =	0x1903	 
val GL.Companion.RGB8: Int
	get() =	0x8051	 
val GL.Companion.RGBA8: Int
	get() =	0x8058	 
val GL.Companion.RGB10_A2: Int
	get() =	0x8059	 
val GL.Companion.TEXTURE_3D: Int
	get() =	0x806F	 
val GL.Companion.TEXTURE_WRAP_R: Int
	get() =	0x8072	 
val GL.Companion.TEXTURE_MIN_LOD: Int
	get() =	0x813A	 
val GL.Companion.TEXTURE_MAX_LOD: Int
	get() =	0x813B	 
val GL.Companion.TEXTURE_BASE_LEVEL: Int
	get() =	0x813C	 
val GL.Companion.TEXTURE_MAX_LEVEL: Int
	get() =	0x813D	 
val GL.Companion.TEXTURE_COMPARE_MODE: Int
	get() =	0x884C	 
val GL.Companion.TEXTURE_COMPARE_FUNC: Int
	get() =	0x884D	 
val GL.Companion.SRGB: Int
	get() =	0x8C40	 
val GL.Companion.SRGB8: Int
	get() =	0x8C41	 
val GL.Companion.SRGB8_ALPHA8: Int
	get() =	0x8C43	 
val GL.Companion.COMPARE_REF_TO_TEXTURE: Int
	get() =	0x884E	 
val GL.Companion.RGBA32F: Int
	get() =	0x8814	 
val GL.Companion.RGB32F: Int
	get() =	0x8815	 
val GL.Companion.RGBA16F: Int
	get() =	0x881A	 
val GL.Companion.RGB16F: Int
	get() =	0x881B	 
val GL.Companion.TEXTURE_2D_ARRAY: Int
	get() =	0x8C1A	 
val GL.Companion.TEXTURE_BINDING_2D_ARRAY: Int
	get() =	0x8C1D	 
val GL.Companion.R11F_G11F_B10F: Int
	get() =	0x8C3A	 
val GL.Companion.RGB9_E5: Int
	get() =	0x8C3D	 
val GL.Companion.RGBA32UI: Int
	get() =	0x8D70	 
val GL.Companion.RGB32UI: Int
	get() =	0x8D71	 
val GL.Companion.RGBA16UI: Int
	get() =	0x8D76	 
val GL.Companion.RGB16UI: Int
	get() =	0x8D77	 
val GL.Companion.RGBA8UI: Int
	get() =	0x8D7C	 
val GL.Companion.RGB8UI: Int
	get() =	0x8D7D	 
val GL.Companion.RGBA32I: Int
	get() =	0x8D82	 
val GL.Companion.RGB32I: Int
	get() =	0x8D83	 
val GL.Companion.RGBA16I: Int
	get() =	0x8D88	 
val GL.Companion.RGB16I: Int
	get() =	0x8D89	 
val GL.Companion.RGBA8I: Int
	get() =	0x8D8E	 
val GL.Companion.RGB8I: Int
	get() =	0x8D8F	 
val GL.Companion.RED_INTEGER: Int
	get() =	0x8D94	 
val GL.Companion.RGB_INTEGER: Int
	get() =	0x8D98	 
val GL.Companion.RGBA_INTEGER: Int
	get() =	0x8D99	 
val GL.Companion.R8: Int
	get() =	0x8229	 
val GL.Companion.RG8: Int
	get() =	0x822B	 
val GL.Companion.R16F: Int
	get() =	0x822D	 
val GL.Companion.R32F: Int
	get() =	0x822E	 
val GL.Companion.RG16F: Int
	get() =	0x822F	 
val GL.Companion.RG32F: Int
	get() =	0x8230	 
val GL.Companion.R8I: Int
	get() =	0x8231	 
val GL.Companion.R8UI: Int
	get() =	0x8232	 
val GL.Companion.R16I: Int
	get() =	0x8233	 
val GL.Companion.R16UI: Int
	get() =	0x8234	 
val GL.Companion.R32I: Int
	get() =	0x8235	 
val GL.Companion.R32UI: Int
	get() =	0x8236	 
val GL.Companion.RG8I: Int
	get() =	0x8237	 
val GL.Companion.RG8UI: Int
	get() =	0x8238	 
val GL.Companion.RG16I: Int
	get() =	0x8239	 
val GL.Companion.RG16UI: Int
	get() =	0x823A	 
val GL.Companion.RG32I: Int
	get() =	0x823B	 
val GL.Companion.RG32UI: Int
	get() =	0x823C	 
val GL.Companion.R8_SNORM: Int
	get() =	0x8F94	 
val GL.Companion.RG8_SNORM: Int
	get() =	0x8F95	 
val GL.Companion.RGB8_SNORM: Int
	get() =	0x8F96	 
val GL.Companion.RGBA8_SNORM: Int
	get() =	0x8F97	 
val GL.Companion.RGB10_A2UI: Int
	get() =	0x906F	 
val GL.Companion.TEXTURE_IMMUTABLE_FORMAT: Int
	get() =	0x912F	 
val GL.Companion.TEXTURE_IMMUTABLE_LEVELS: Int
	get() =	0x82DF
