package tj.ilhom.musicappplayer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import tj.ilhom.musicappplayer.service.model.MusicItem

class MusicService : Service() {

    companion object {
        const val NOTIFICATION_MODEL = "notificationModel"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { showNotification(it.getSerializableExtra(NOTIFICATION_MODEL) as MusicItem) }
        return START_NOT_STICKY
    }

    fun showNotification(model: MusicItem) {
        val notification = NotificationUtil(this).createNotification(model)
        val mediaSession = MediaSessionUtil(baseContext)
        mediaSession.initSession(model)
        startForeground(13, notification)
    }
}