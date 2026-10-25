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

import android.app.ActionBar
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import net.whily.kotland.Render2DActivity
import net.whily.kotland.Util
import net.whily.kotsci.phys.NBody

class ShowActivity : Render2DActivity(), ActionBar.OnNavigationListener {
  private var bar: ActionBar? = null
  private var configId: Int = 0

  override fun onCreate(icicle: Bundle?) {
    super.onCreate(icicle)

    renderView = ShowView(this, configId)
    setContentView(renderView)
    setTitle("")

    bar = actionBar
    bar?.setHomeButtonEnabled(true)

    Util.requestImmersiveMode(this)

    // Show navigation list, which is at the left side of action bar.
    val configNames = NBody.threeBodyConfigs().map { it.name }
    val configAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, configNames)
    configAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
    bar?.navigationMode = ActionBar.NAVIGATION_MODE_LIST
    bar?.setListNavigationCallbacks(configAdapter, this)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.show, menu)

    return super.onCreateOptionsMenu(menu)
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.settings -> {
        startActivity(Intent(this, SettingsActivity::class.java))
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onNavigationItemSelected(itemPosition: Int, itemId: Long): Boolean {
    if (itemPosition != configId) {
      configId = itemPosition
      renderView.pause()
      renderView = ShowView(this, configId)
      setContentView(renderView)
      renderView.resume()
    }

    return true
  }
}
