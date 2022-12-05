package tj.ilhom.musicappplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.core.extention.musicDrawable
import tj.ilhom.musicappplayer.extention.newActionIntent
import tj.ilhom.musicappplayer.extention.newPendingIntent
import tj.ilhom.musicappplayer.service.model.MusicItem

class NotificationUtil(private val context: Context) {

    companion object {
        private var notification: Notification? = null
        const val PREVIOUS = "PREVIOUS"
        const val PLAY = "PLAY"
        const val NEXT = "NEXT"
        const val EXIT = "EXIT"
        private var musicItem: MusicItem? = null
    }

    fun notificationModel(): MusicItem? {
        return musicItem
    }

    fun createNotification(
        model: MusicItem
    ): Notification {
        musicItem = model

        val isPlay = MusicPlayerUtil().player()?.isPlaying == true || model.isPlay

        val mediaSession = MediaSessionUtil(context)
        val drawable = model.musicPath.musicDrawable(context)
            ?: ContextCompat.getDrawable(context, R.drawable.music_icon)

        val image = drawable?.toBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight)

        val prevIntent = context.newActionIntent(PREVIOUS)
        val prevPendingIntent = context.newPendingIntent(prevIntent)

        val playIntent = context.newActionIntent(PLAY)
        val playPendingIntent = context.newPendingIntent(playIntent)

        val nextIntent = context.newActionIntent(NEXT)
        val nextPendingIntent = context.newPendingIntent(nextIntent)

        val exitIntent = context.newActionIntent(EXIT)
        val exitPendingIntent = context.newPendingIntent(exitIntent)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createNotificationChannel()

        val notificationBuilder =
            NotificationCompat.Builder(context, context.applicationContext.packageName)
        with(notificationBuilder) {
            setContentTitle(model.title.replace(".mp3", ""))
            setContentText(model.artist)
            setSmallIcon(R.drawable.music_icon)
            setLargeIcon(image)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSession.mediaSession().sessionToken)
            )
            priority = NotificationCompat.PRIORITY_HIGH
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            addAction(R.drawable.previous_icon, PREVIOUS, prevPendingIntent)

            addAction(playIcon(isPlay), PLAY, playPendingIntent)
            addAction(R.drawable.next_icon, NEXT, nextPendingIntent)
            if (!isPlay)
                addAction(R.drawable.exit_icon, EXIT, exitPendingIntent)
        }
        notification = notificationBuilder.build()
        return notification!!
    }

    fun clean() {
        notification = null
    }

    fun playIcon(boolean: Boolean): Int {
        return if (boolean) {
            R.drawable.pause_icon
        } else {
            R.drawable.play_icon
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                context.applicationContext.packageName,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val manager = context.getSystemService(
                NotificationManager::class.java
            )
            manager.createNotificationChannel(serviceChannel)
        }
    }
}