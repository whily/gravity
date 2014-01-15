/**
  * 2D demo activity.
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
import android.widget.{ArrayAdapter, LinearLayout}
import net.whily.scasci.phys._
import net.whily.scaland.{Render2DActivity, Render2DView, Util}

class ShowActivity extends Render2DActivity with ActionBar.OnNavigationListener {
  private var bar: ActionBar = null
  private var configId: Int   = 0
  
  override def onCreate(icicle: Bundle) { 
    super.onCreate(icicle)

    renderView = new ShowView(this, configId)
    setContentView(renderView)  
    setTitle("")
    
    bar = getActionBar
    bar.setHomeButtonEnabled(true)

    Util.requestImmersiveMode(this)

    // Show navigation list, which is at the left side of action bar.
    val configNames = NBody.threeBodyConfigs map (_.name)
    val configAdapter = new ArrayAdapter[String](this, android.R.layout.simple_spinner_item, configNames) 
    configAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    bar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST)
    bar.setListNavigationCallbacks(configAdapter, this)
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

  override def onNavigationItemSelected(itemPosition: Int, itemId: Long): Boolean = {
    if (itemPosition != configId) {
      configId = itemPosition
      renderView.pause()
      renderView = new ShowView(this, configId)
      setContentView(renderView)
      renderView.resume()
    }    
    
    true
  }
}

class ShowView(context: Context, configId: Int) extends Render2DView(context) with Runnable {
  val fpsLimit = 50
  val drawInterval = 1000 / fpsLimit // In ms.
  val config = NBody.threeBodyConfigs(configId)
  val sim = new NBody(config, 0.0001)
  var time = System.currentTimeMillis()
  var simTime = 0.0
  val colors = Array(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)
   assert(sim.bodies.length <= colors.length)
  var sf = 0.0
  var orbit: List[(Double, Double, Int)] = List()   // (x, y, color)
  val paint = new Paint()
  paint.setAntiAlias(true)
  paint.setStyle(Paint.Style.FILL)

  override def resume() {
    super.resume()
    time = System.currentTimeMillis()
  }

  def drawOn(canvas: Canvas) {
    val elapsed = System.currentTimeMillis() - time
    // Limit FPS.
    if (elapsed < drawInterval)
      Thread.sleep(drawInterval - elapsed)

    val now = System.currentTimeMillis()
    // Slow down simulation by dividing 5.
    simTime += (now - time) / 1000.0 / 5.0 
    time = now
    sim.evolve("rk4", simTime)

    val showOrbit = true
    val showInfo = false

    canvas.drawColor(Color.BLACK)
 
    val width = canvas.getWidth()
    val height = canvas.getHeight()

    if (sf == 0.0) 
      sf = scalingFactor(width, height)

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

  private def drawBody(body: Body, width: Int, height: Int, canvas: Canvas, color: Int) {
    drawCartesianXY(body.pos.x, body.pos.y, width, height, canvas, color, 18)
  }

  private def drawCartesianXY(x: Double, y: Double, width: Int, height: Int, canvas: Canvas, color: Int, radius: Int) {
    val screenX = (x * sf).toInt + width / 2
    val screenY = (y * sf).toInt + height / 2
    paint.setColor(color)
    canvas.drawCircle(screenX, screenY, radius, paint)
  }
}
