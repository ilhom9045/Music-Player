package tj.ilhom.musicappplayer.service

import android.content.Context
import android.media.MediaPlayer
import tj.ilhom.musicappplayer.extention.newActionIntent

class MusicPlayerUtil(private val context: Context) {

    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }

    private var playerListener: MusicPlayerListener? = null

    init {
        mediaPlayer.setOnCompletionListener {
            context.sendBroadcast(context.newActionIntent(NotificationUtil.NEXT))
        }
    }

    fun setListener(musicPlayerListener: MusicPlayerListener) {
        this.playerListener = musicPlayerListener
    }

    fun onSeekTo(pos: Int) {
        mediaPlayer.seekTo(pos)
        playerListener?.onSeekTo(pos.toLong())
    }

    fun player(): MediaPlayer {
        return mediaPlayer
    }

    fun play(path: String) {
        try {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(path)
            mediaPlayer.prepare()
            play()
            playerListener?.onPlay()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    fun pause() {
        mediaPlayer.pause()
    }

    fun stop() {
        mediaPlayer.stop()
    }

    fun play() {
        mediaPlayer.start()
    }

    fun clean() {
        mediaPlayer.release()
    }

    interface MusicPlayerListener {

        fun onPlay()

        fun onSeekTo(pos: Long)

    }
}