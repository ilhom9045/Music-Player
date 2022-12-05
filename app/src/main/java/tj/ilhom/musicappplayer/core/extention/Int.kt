package tj.ilhom.musicappplayer.core.extention

import android.content.res.Resources
import kotlin.math.roundToInt

fun Int.dpToPx(): Int = (this * Resources.getSystem().displayMetrics.density).roundToInt()

fun Int.pxToDp(): Int = (this / Resources.getSystem().displayMetrics.density).roundToInt()