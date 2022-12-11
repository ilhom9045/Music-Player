package tj.ilhom.musicappplayer.extention

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import androidx.core.graphics.drawable.toDrawable
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.repository.TimerRepository

fun String.imageArtist(): ByteArray? {
    val retriever = MediaMetadataRetriever()
    retriever.setDataSource(this)
    return retriever.embeddedPicture
}

fun String.musicImage(context: Context): Bitmap? {
    val artistImage = this.imageArtist()
    return if (artistImage != null) {
        BitmapFactory.decodeByteArray(artistImage, 0, artistImage.size)
    } else {
        BitmapFactory.decodeResource(context.resources, R.drawable.music_icon)
    }
}

fun String.musicDrawable(context: Context): Drawable? {
    val icon = musicImage(context)
    return icon?.toDrawable(context.resources)
}

fun String.timeToDuration(): Long {
    return TimerRepository.Base().timeToDuration(this)
}

fun String.durationToTime(): String {
    return TimerRepository.Base().durationToTime(this)
}