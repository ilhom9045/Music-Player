package tj.ilhom.musicappplayer.service

import android.content.Context
import android.content.Intent
import kotlinx.coroutines.*
import tj.ilhom.musicappplayer.extention.startMusicService
import tj.ilhom.musicappplayer.extention.toMusicItem
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao

class MusicManager(
    private val context: Context,
    private val musicDao: MusicDao,
    private val coroutineScope: CoroutineScope,
    private val playerUtil: MusicPlayerUtil,
    private val mediaSession: MediaSessionUtil,
    private val notificationUtil: NotificationUtil
) {

    private val model = notificationUtil.notificationModel()

    init {

        mediaSession.mediaSession()
    }

    fun previous(listener: MusicNotificationBroadcast.MusicNotificationListener? = null) {
        coroutineScope.launch {
            val item = model?.id?.let { it1 -> musicDao.previous(it1).toMusicItem() }
            launch(Dispatchers.Main) {
                item?.let { it1 ->
                    listener?.previous(it1)
                    context.startMusicService(it1)
                }
            }
        }
    }

    fun play(listener: MusicNotificationBroadcast.MusicNotificationListener? = null) {
        val isPlay = playerUtil.player()?.isPlaying == true
        if (isPlay) {
            listener?.pause()
            playerUtil.pause()
        } else {
            listener?.play()
            playerUtil.play()
        }
        model?.isPlay = false
        model?.let { it1 ->
            context.startMusicService(it1)
        }
    }

    fun next(listener: MusicNotificationBroadcast.MusicNotificationListener? = null) {
        coroutineScope.launch {
            val item = model?.id?.let { it1 -> musicDao.next(it1).toMusicItem() }
            launch(Dispatchers.Main) {
                item?.let { it1 ->
                    listener?.next(it1)
                    context.startMusicService(it1)
                }
            }
        }
    }

    fun exit() {
        notificationUtil.clean()
        playerUtil.clean()
        mediaSession.clean()
        context.stopService(Intent(context, MusicService::class.java))
    }

    interface MusicCurrentTime{

        fun onTimeChange()

    }

}