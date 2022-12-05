package tj.ilhom.musicappplayer.repository.musicrepository

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.core.extention.musicDrawable
import javax.inject.Inject

interface ReadMediaIcon {

    fun readIcon(path: String): Drawable?

    class Base @Inject constructor(@ApplicationContext private val context: Context) : ReadMediaIcon {

        override fun readIcon(path: String): Drawable? {
            return path.musicDrawable(context)
        }
    }
}