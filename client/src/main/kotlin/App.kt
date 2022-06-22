import kotlinx.browser.document
import kotlinx.browser.window
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLCanvasElement
import org.w3c.dom.events.*
import org.w3c.dom.Window

class App(val canvas : HTMLCanvasElement, val overlay : HTMLDivElement) {

  val keysPressed = HashSet<String>()

  val gl = (canvas.getContext("webgl2", object{val alpha = false}) ?: throw Error("Browser does not support WebGL2")) as WebGL2RenderingContext //#alpha# never make canvas transparent ˙HUN˙ ne legyen áttetsző a vászon  

  val scene = Scene(gl)//#scene# this object is responsible for resource allocation and drawing ˙HUN˙ ez az objektum felel az erőforrások kezeléséért és kirajzolásáért
  init {
    resize()//## we adjust render resolution in a separate method, as we will also need it when the canvas is resized ˙HUN˙ rajzolási felbontás beállítása külön metódusban, mert ablakátméretezéskor is kell majd ugyanez
  }

  fun resize() {
    canvas.width = canvas.clientWidth//#canvas.width# rendering resolution ˙HUN˙ rajzolási felbontás #canvas.clientWidth# canvas size ˙HUN˙ a vászon mérete 
    canvas.height = canvas.clientHeight
    scene.resize(gl, canvas)
  }

  @Suppress("UNUSED_PARAMETER")
  fun registerEventHandlers() {
    document.onkeydown =  { //#{# locally defined function
      event : KeyboardEvent ->
      keysPressed.add( keyNames[event.keyCode] )
    }

    document.onkeyup = { 
      event : KeyboardEvent ->
      keysPressed.remove( keyNames[event.keyCode] )
    }

    canvas.onmousedown = { 
      event : MouseEvent ->
      // event.x.toInt()
      // event.y.toInt()
      scene.camera.mouseDown()
    }

    canvas.onmousemove = { 
      event : MouseEvent ->
      scene.camera.mouseMove(event)
      event.stopPropagation()
    }

    canvas.onmouseup = { 
      event : MouseEvent ->
      scene.camera.mouseUp()
    }

    canvas.onmouseout = { 
      event : Event ->
      event // This line is a placeholder for event handling code. It has no effect, but avoids the "unused parameter" warning.
    }

    window.onresize = { resize()}

    window.requestAnimationFrame {//#requestAnimationFrame# trigger rendering
      update()//#update# this method is responsible; for drawing a frame
    }
  }  

  fun update() {
    scene.update(gl, keysPressed)    
    window.requestAnimationFrame { update() }
  }
}

fun main() {
  val canvas = document.getElementById("canvas") as HTMLCanvasElement
  val overlay = document.getElementById("overlay") as HTMLDivElement
  overlay.innerHTML = """<font color="red">WebGL</font>"""

  try{
    val app = App(canvas, overlay)//#app# from this point on,; this object is responsible; for handling everything
    app.registerEventHandlers()//#registerEventHandlers# we implement this; to make sure the app; knows when there is; something to do
  } catch(e : Error) {
    console.error(e.message)
  }
}