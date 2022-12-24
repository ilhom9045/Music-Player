package com.ilhom.core_ui.extention

import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity

fun AppCompatActivity.transparentToolbar() {
    val windowInsetsController =
        ViewCompat.getWindowInsetsController(window.decorView) ?: return
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())

}

fun AppCompatActivity.showToolbar() {
    val windowInsetsController =
        ViewCompat.getWindowInsetsController(window.decorView) ?: return
    windowInsetsController.systemBarsBehavior =
        WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_TOUCH
    windowInsetsController.show(WindowInsetsCompat.Type.systemBars())

}

fun FragmentActivity.transaction(
    container_id: Int, fragment: Fragment, addToBackStack: Boolean = false, tag: String? = null
) {
    supportFragmentManager.beginTransaction().apply {
        replace(container_id, fragment, tag)
        if (addToBackStack) {
            addToBackStack(tag)
        }
        commit()
    }
}
