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

    setTitle("")
    
    bar = getActionBar
    bar.setHomeButtonEnabled(true)
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
  val sim = NBody.figure8Sim
  var time = System.currentTimeMillis()
  var simTime = 0.0
  var orbit: List[(Double, Double)] = List()
  (new Thread(this)).start()

  override def onDraw(canvas: Canvas) {
    val showOrbit = true
    super.onDraw(canvas)
    canvas.drawColor(Color.BLACK)

    val width = canvas.getWidth()
    val height = canvas.getHeight()

    if (showOrbit) {
      orbit = List((sim.bodies(0).pos.x, sim.bodies(0).pos.y)) ::: orbit

      for (coord <- orbit) {
        val (x, y) = coord
        drawCartesianXY(x, y, width, height, canvas, Color.GRAY, 2)
      }
    }

    drawBody(sim.bodies(0), width, height, canvas, Color.GREEN)
    drawBody(sim.bodies(1), width, height, canvas, Color.YELLOW)
    drawBody(sim.bodies(2), width, height, canvas, Color.BLUE)
  }

  override def run() {
    while(!Thread.currentThread().isInterrupted()) {
      try{
        val now = System.currentTimeMillis()
        val elapsed = (now - time) / 1000.0
        time = now
        simTime += elapsed / 5.0 // Slow down simulation
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
    val scaling = 400
    val screenX = (x * scaling).toInt + width / 2
    val screenY = (y * scaling).toInt + height / 2
    paint.setColor(color)
    canvas.drawCircle(screenX, screenY, radius, paint)
  }
}
