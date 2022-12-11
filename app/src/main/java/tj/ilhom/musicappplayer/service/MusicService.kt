package tj.ilhom.musicappplayer.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import tj.ilhom.musicappplayer.extention.toLatestMusicModel
import tj.ilhom.musicappplayer.repository.localStorage.latestMusic.SaveLatestMusic
import tj.ilhom.musicappplayer.service.model.MusicItem
import javax.inject.Inject

@AndroidEntryPoint
class MusicService : Service() {

    companion object {
        const val NOTIFICATION_MODEL = "notificationModel"
    }

    @Inject
    lateinit var mediaSessionUtil: MediaSessionUtil

    @Inject
    lateinit var notificationUtil: NotificationUtil


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let { showNotification(it.getSerializableExtra(NOTIFICATION_MODEL) as MusicItem) }
        return START_NOT_STICKY
    }

    fun showNotification(model: MusicItem) {
        val notification = notificationUtil.createNotification(model, mediaSessionUtil)
        mediaSessionUtil.initSession(model)
        startForeground(13, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaSessionUtil.mediaSession().release()
        stopForeground(true)
    }
}