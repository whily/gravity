/**
 * Search activity for World Metro.
 *
 * @author  Yujian Zhang <yujian{dot}zhang[at]gmail(dot)com>
 *
 * License: 
 *   GNU General Public License v2
 *   http://www.gnu.org/licenses/gpl-2.0.html
 * Copyright (C) 2014 Yujian Zhang
 */

package net.whily.android.gravity

import scala.collection.mutable
import android.app.{ActionBar, Activity}
import android.content.{Intent, Context}
import android.graphics.{Canvas, Color, Paint}
import android.os.Bundle
import android.view.{Menu, MenuItem, MotionEvent, View}
import android.widget.{LinearLayout}
import net.whily.scasci.phys._

class ShowActivity extends Activity {
  private var bar: ActionBar = null
  private var view: ShowView = null
  
  override def onCreate(icicle: Bundle) { 
    super.onCreate(icicle)

    view = new ShowView(this)
    setContentView(view)  
    
    bar = getActionBar
    bar.setHomeButtonEnabled(true)

    // Enter immersive mode.
    getWindow().getDecorView().
      setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | 
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_IMMERSIVE)
  }
   
  override def onCreateOptionsMenu(menu: Menu): Boolean = {
    getMenuInflater().inflate(R.menu.show, menu)
    
    return super.onCreateOptionsMenu(menu)
  }  
  
  override def onOptionsItemSelected(item: MenuItem): Boolean = {
    item.getItemId match {
      case R.id.settings =>  
        startActivity(new Intent(this, classOf[SettingsActivity]))
        true
    }
  }
}

class ShowView(context: Context) extends View(context) with Runnable {
  val paint = new Paint()
  paint.setAntiAlias(true)
  paint.setStyle(Paint.Style.FILL)
  val config = NBody.brouckeA10Config
  val sim = new NBody(config, 0.0001)
  var time = System.currentTimeMillis()
  var simTime = 0.0
  var orbit: List[(Double, Double, Int)] = List()   // (x, y, color)
  (new Thread(this)).start()

  private def scalingFactor(width: Int, height: Int): Double = {
    var maxX = 0.0
    var maxY = 0.0
    for (body <- config.bodies) {
      if (math.abs(body.pos.x) > maxX) maxX = math.abs(body.pos.x)
      if (math.abs(body.pos.y) > maxY) maxY = math.abs(body.pos.y)
    }
    // 0.8 i used so that the figure is not occupying the whole canvas.
    0.8 * math.min(width / 2 / maxX, width / 2 / maxY)
  }

  override def onDraw(canvas: Canvas) {
    val showOrbit = true
    val showInfo = false
    super.onDraw(canvas)
    canvas.drawColor(Color.BLACK)
    val colors = Array(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)
    assert(sim.bodies.length <= colors.length)

    val width = canvas.getWidth()
    val height = canvas.getHeight()

    if (showOrbit) {
      if (simTime < config.period)
        for (i <- 0 until sim.bodies.length)
          orbit = (sim.bodies(i).pos.x, sim.bodies(i).pos.y, colors(i)) :: orbit

      for (coord <- orbit) {
        val (x, y, color) = coord
        drawCartesianXY(x, y, width, height, canvas, color, 2)
      }
    }

    for (i <- 0 until sim.bodies.length) 
      drawBody(sim.bodies(i), width, height, canvas, colors(i))
  }

  override def run() {
    while(!Thread.currentThread().isInterrupted()) {
      try{
        val now = System.currentTimeMillis()
        val elapsed = (now - time) / 1000.0
        time = now
        simTime += elapsed / 10.0 // Slow down simulation
        sim.evolve("rk4", simTime)
        // TODO: Try FPS limitation here.
      } catch {
        case ex: InterruptedException => Thread.currentThread().interrupt()
      }
      postInvalidate()
    }
  }

  private def drawBody(body: Body, width: Int, height: Int, canvas: Canvas, color: Int) {
    drawCartesianXY(body.pos.x, body.pos.y, width, height, canvas, color, 18)
  }

  private def drawCartesianXY(x: Double, y: Double, width: Int, height: Int, canvas: Canvas, color: Int, radius: Int) {
    val sf = scalingFactor(width, height)
    val screenX = (x * sf).toInt + width / 2
    val screenY = (y * sf).toInt + height / 2
    paint.setColor(color)
    canvas.drawCircle(screenX, screenY, radius, paint)
  }
}
