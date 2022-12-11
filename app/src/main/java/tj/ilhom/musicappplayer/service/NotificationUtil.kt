package tj.ilhom.musicappplayer.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import tj.ilhom.musicappplayer.R
import tj.ilhom.musicappplayer.extention.musicDrawable
import tj.ilhom.musicappplayer.extention.newActionIntent
import tj.ilhom.musicappplayer.extention.newPendingIntent
import tj.ilhom.musicappplayer.modules.StartActivity
import tj.ilhom.musicappplayer.service.model.MusicItem
import java.util.concurrent.atomic.AtomicInteger

class NotificationUtil(private val context: Context, private val musicPlayerUtil: MusicPlayerUtil) {

    companion object {
        const val PREVIOUS = "PREVIOUS"
        const val PLAY = "PLAY"
        const val NEXT = "NEXT"
        const val EXIT = "EXIT"
        const val STOP = "STOP"
        private var musicItem: MusicItem? = null
        val requestCodeProvider = AtomicInteger(SystemClock.elapsedRealtime().toInt())
    }

    fun notificationModel(): MusicItem? {
        return musicItem
    }

    fun createNotification(
        model: MusicItem, mediaSessionUtil: MediaSessionUtil
    ): Notification {
        musicItem = model

        val isPlay = musicPlayerUtil.player()?.isPlaying == true || model.isPlay

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

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                requestCodeProvider.incrementAndGet(),
                Intent(context, StartActivity::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
                } else {
                    PendingIntent.FLAG_UPDATE_CURRENT
                }
            )


        val notificationBuilder =
            NotificationCompat.Builder(context, context.applicationContext.packageName)
        with(notificationBuilder)
        {
            setContentTitle(model.title.replace(".mp3", ""))
            setContentText(model.artist)
            setSmallIcon(R.drawable.music_icon)
            setLargeIcon(image)
            setStyle(
                androidx.media.app.NotificationCompat.MediaStyle()
                    .setMediaSession(mediaSessionUtil.mediaSession().sessionToken)
            )
            priority = NotificationCompat.PRIORITY_HIGH
            setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            addAction(R.drawable.previous_icon, PREVIOUS, prevPendingIntent)
            setContentIntent(pendingIntent)
            addAction(playIcon(isPlay), PLAY, playPendingIntent)
            addAction(R.drawable.next_icon, NEXT, nextPendingIntent)
            if (!isPlay)
                addAction(R.drawable.exit_icon, EXIT, exitPendingIntent)
        }
        return notificationBuilder.build()
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