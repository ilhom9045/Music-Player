package com.ilhom.core_ui.extention

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment

inline fun <reified T : View> Fragment.findViewById(@IdRes id: Int): T {
    return requireView().findViewById(id)
}

fun Fragment.showLongToast(message: String) {
    requireContext().showLongToast(message)
}

fun Fragment.transaction(
    container_id: Int, fragment: Fragment, addToBackStack: Boolean = false, tag: String? = null
) {
    requireActivity().supportFragmentManager.beginTransaction().apply {
        replace(container_id, fragment, tag)
        if (addToBackStack) {
            addToBackStack(tag)
        }
        commit()
    }
}