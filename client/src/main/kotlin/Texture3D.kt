import org.khronos.webgl.Uint8Array
import org.khronos.webgl.WebGLTexture
import org.khronos.webgl.get
import org.khronos.webgl.set
import org.w3c.dom.CanvasRenderingContext2D
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.Image
import kotlin.browser.document
import org.khronos.webgl.WebGLRenderingContext as GL
import vision.gears.webglmath.Texture
import vision.gears.webglmath.TEXTURE_3D

class Texture3D(
        gl : WebGL2RenderingContext,
        val mediaFileUrl : String,
        val width: Int = 256,
        val height: Int = 256,
        val depth: Int = 256
) : Texture {

    override val glTexture : WebGLTexture? = gl.createTexture()

    init {

        val image = Image()
        image.onload = {

            val canvas = document.createElement( localName = "canvas" ) as HTMLCanvasElement
            val context = canvas.getContext( contextId = "2d" ) as CanvasRenderingContext2D

            canvas.width = image.width
            canvas.height = image.height

            context.drawImage( image = image, dx = 0.0, dy = 0.0 )

            val imageData = context.getImageData( sx = 0.0, sy = 0.0, sw = image.width.toDouble(), sh = image.height.toDouble() )

            val data3d = Uint8Array( length = 256*256*256 )
            for (i: Int in 0..255)
                for (j: Int in 0..255)
                    for (k: Int in 0..255) {
                        var v : Byte = imageData.data[(i + 4096 * j + 256 * (k % 16) + 4096 * 256 * kotlin.math.floor(x = k / 16.0f).toInt()) * 4]
                        //var v = ((i - 128) * (i - 128) + (j - 128) * (j - 128) + (k - 128) * (k - 128)).toDouble()
                        //v /= 128.0*128.0*3.0
                        //data3d[i + 256 * (j + 256 * k)] = (1.0-v * 256.0).toByte()
                        data3d[i + 256 * (j + 256 * k)] = v
                    }

            gl.bindTexture( target = GL.TEXTURE_3D, texture = glTexture )
            gl.texImage3D( target = GL.TEXTURE_3D, level = 0, internalformat = GL.R8, width = this.width, height = this.height, depth = this.depth, border = 0, format = GL.RED, type = GL.UNSIGNED_BYTE, source = data3d )
            gl.texParameteri( target = GL.TEXTURE_3D, pname = GL.TEXTURE_MAG_FILTER, param = GL.LINEAR )
            gl.texParameteri( target = GL.TEXTURE_3D, pname = GL.TEXTURE_MIN_FILTER, param = GL.LINEAR_MIPMAP_LINEAR )
            gl.generateMipmap( target = GL.TEXTURE_3D );
            gl.bindTexture( target = GL.TEXTURE_3D, texture = null )
            console.log("3dtexture loaded")
        }

        image.src = mediaFileUrl

    }
}
