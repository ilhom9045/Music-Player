package tj.ilhom.musicappplayer.core.extention

import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.marginBottom
import androidx.core.view.marginLeft
import androidx.core.view.marginRight
import androidx.core.view.marginTop

fun View.changeSize(width: Int, height: Int) {
    val mylayoutParams = layoutParams as LinearLayout.LayoutParams
    mylayoutParams.width = width
    mylayoutParams.height = height
    layoutParams = mylayoutParams
}


fun View.margins(left: Int = marginLeft, top: Int = marginTop, right: Int = marginRight, bottom: Int = marginBottom) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val p = layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left, top, right, bottom)
        requestLayout()
    }
}
