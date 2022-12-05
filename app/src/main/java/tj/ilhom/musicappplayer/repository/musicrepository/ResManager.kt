package tj.ilhom.musicappplayer.repository.musicrepository

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.R
import javax.inject.Inject

interface ResManager {

    fun drawable(@DrawableRes id: Int): Drawable?

    class Base @Inject constructor(@ActivityContext private val context: Context) : ResManager {

        @SuppressLint("UseCompatLoadingForDrawables")
        override fun drawable(id: Int): Drawable? {
            return ContextCompat.getDrawable(context, id)
        }
    }
}