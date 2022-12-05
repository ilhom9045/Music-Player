package tj.ilhom.musicappplayer.service

import android.media.MediaPlayer

class MusicPlayerUtil {

    private companion object {
        private var mediaPlayer: MediaPlayer? = null
    }

    fun player(): MediaPlayer? {
        return mediaPlayer
    }

    fun play(path: String) {
        try {
            if (mediaPlayer == null) mediaPlayer = MediaPlayer()
            mediaPlayer!!.reset()
            mediaPlayer!!.setDataSource(path)
            mediaPlayer!!.prepare()
            mediaPlayer!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun play() {
        mediaPlayer?.start()
    }

    fun clean() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

}