package tj.ilhom.musicappplayer.core.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import tj.ilhom.musicappplayer.R

class ToolbarUtil(private val appCompatActivity: AppCompatActivity) {

    private var toolbar: Toolbar? = null

    private val v: View = appCompatActivity.window.decorView.rootView

    fun init(@IdRes id: Int = R.id.base_toolbar) {
        toolbar = v.findViewById(id)
        appCompatActivity.setSupportActionBar(toolbar)
    }

    fun setTitle(title: String?) {
        appCompatActivity.supportActionBar?.title = title
    }

    fun setDisplayHomeEnable(enable: Boolean) {
        appCompatActivity.supportActionBar?.setDisplayHomeAsUpEnabled(enable)
    }
}