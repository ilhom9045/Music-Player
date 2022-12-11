package tj.ilhom.musicappplayer.service

import android.content.Context
import android.media.MediaPlayer
import tj.ilhom.musicappplayer.extention.newActionIntent
import tj.ilhom.musicappplayer.extention.nextMusic

class MusicPlayerUtil(private val context: Context) {

    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }

    private var playerListener: MusicPlayerListener? = null

    fun setListener(musicPlayerListener: MusicPlayerListener) {
        this.playerListener = musicPlayerListener
    }

    fun onSeekTo(pos: Int) {
        try {
            mediaPlayer.seekTo(pos)
            playerListener?.onSeekTo(pos.toLong())
        } catch (e: Exception) {
            e.printStackTrace()
        }
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
            mediaPlayer.setOnCompletionListener {
                context.nextMusic()
            }
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
        mediaPlayer.setOnCompletionListener {}
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