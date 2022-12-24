package tj.ilhom.musicappplayer.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import com.ilhom.core.music.MusicManager
import tj.ilhom.musicappplayer.service.NotificationUtil.Companion.EXIT
import tj.ilhom.musicappplayer.service.NotificationUtil.Companion.NEXT
import tj.ilhom.musicappplayer.service.NotificationUtil.Companion.PLAY
import tj.ilhom.musicappplayer.service.NotificationUtil.Companion.PREVIOUS
import tj.ilhom.musicappplayer.service.NotificationUtil.Companion.STOP
import tj.ilhom.musicappplayer.service.model.MusicItem
import javax.inject.Inject

@AndroidEntryPoint
class MusicNotificationBroadcast : BroadcastReceiver() {

    @Inject
    lateinit var manager: MusicManager

    companion object {
        var musicNotificationListener: MusicNotificationListener? = null
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            when (intent?.action) {
                PREVIOUS -> {
                    manager.previous(musicNotificationListener)
                }
                PLAY -> {
                    manager.play(musicNotificationListener)
                }
                NEXT -> {
                    manager.next(musicNotificationListener)
                }
                STOP -> {
                    manager.stop(musicNotificationListener)
                }
                EXIT -> {
                    musicNotificationListener?.exit()
                    manager.exit()
                }
                else -> {}
            }
        }
    }

    interface MusicNotificationListener {

        fun previous(item: MusicItem)

        fun play()

        fun pause()

        fun next(item: MusicItem)

        fun exit()

    }
}