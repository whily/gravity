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
import android.graphics.Canvas
import android.os.Bundle
import android.view.{Menu, MenuItem, MotionEvent, View}
import android.widget.{LinearLayout}

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

class ShowView(context: Context) extends View(context) {
  override def onDraw(canvas: Canvas) {
  }
}
