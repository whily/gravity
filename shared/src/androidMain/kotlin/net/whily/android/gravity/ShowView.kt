/**
 * 2D view for N-body simulation.
 *
 * @author  Yujian Zhang <yujian{dot}zhang[at]gmail(dot)com>
 *
 * License:
 *   GNU General Public License v2
 *   http://www.gnu.org/licenses/gpl-2.0.html
 * Copyright (C) 2014 Yujian Zhang
 */

package net.whily.android.gravity

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.math.abs
import net.whily.kotland.Render2DView
import net.whily.kotsci.phys.Body
import net.whily.kotsci.phys.NBody

class ShowView(context: Context, configId: Int) : Render2DView(context), Runnable {
  val fpsLimit = 50
  val drawInterval = 1000 / fpsLimit // In ms.
  val config = NBody.threeBodyConfigs()[configId]
  val sim = NBody(config, 0.001)
  var time = System.currentTimeMillis()
  var simTime = 0.0
  val colors = intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW, Color.CYAN, Color.MAGENTA)
  var sf = 0.0
  val orbit = mutableListOf<Triple<Double, Double, Int>>()   // (x, y, color)
  val paint = Paint()

  init {
    assert(sim.bodies.size <= colors.size)
    paint.isAntiAlias = true
    paint.style = Paint.Style.FILL
  }

  override fun resume() {
    super.resume()
    time = System.currentTimeMillis()
  }

  override fun drawOn(canvas: Canvas) {
    val elapsed = System.currentTimeMillis() - time
    // Limit FPS.
    if (elapsed < drawInterval)
      Thread.sleep(drawInterval - elapsed)

    val now = System.currentTimeMillis()
    // Slow down simulation by dividing 5.
    simTime += (now - time) / 1000.0 / 5.0
    time = now
    sim.evolve(simTime)

    val showOrbit = true
    val showInfo = false

    canvas.drawColor(Color.BLACK)

    val width = canvas.width
    val height = canvas.height

    if (sf == 0.0)
      sf = scalingFactor(width, height)

    if (showOrbit) {
      if (simTime < config.period) {
        for (i in sim.bodies.indices)
          orbit.add(Triple(sim.bodies[i].pos.x, sim.bodies[i].pos.y, colors[i]))
      }

      for (coord in orbit)
        drawCartesianXY(coord.first, coord.second, width, height, canvas, coord.third, 2)
    }

    for (i in sim.bodies.indices)
      drawBody(sim.bodies[i], width, height, canvas, colors[i])
  }

  private fun scalingFactor(width: Int, height: Int): Double {
    var maxX = 0.0
    var maxY = 0.0
    for (body in config.bodies) {
      if (abs(body.pos.x) > maxX) maxX = abs(body.pos.x)
      if (abs(body.pos.y) > maxY) maxY = abs(body.pos.y)
    }
    // 0.8 i used so that the figure is not occupying the whole canvas.
    return 0.8 * minOf(width / 2 / maxX, width / 2 / maxY)
  }

  private fun drawBody(body: Body, width: Int, height: Int, canvas: Canvas, color: Int) {
    drawCartesianXY(body.pos.x, body.pos.y, width, height, canvas, color, 18)
  }

  private fun drawCartesianXY(x: Double, y: Double, width: Int, height: Int, canvas: Canvas, color: Int, radius: Int) {
    val screenX = (x * sf).toInt() + width / 2
    val screenY = (-y * sf).toInt() + height / 2
    paint.color = color
    canvas.drawCircle(screenX.toFloat(), screenY.toFloat(), radius.toFloat(), paint)
  }
}
