package tj.ilhom.musicappplayer.service

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.view.KeyEvent
import android.view.KeyEvent.*
import com.ilhom.core_ui.extention.nextMusic
import com.ilhom.core_ui.extention.playMusic
import com.ilhom.core_ui.extention.previousMusic
import com.ilhom.core_ui.extention.stopMusic

class MediaSessionCallBack(
    private val context: Context,
    private val mediaSessionCompat: MediaSessionCompat,
    private val mediaPlayer: MediaPlayer,
) : MediaSessionCompat.Callback() {

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        val playbackSpeed = if (mediaPlayer.isPlaying) 1F else 0F
        mediaPlayer.seekTo(pos.toInt())
        val playBackStateNew = PlaybackStateCompat.Builder()
            .setState(
                PlaybackStateCompat.STATE_PLAYING,
                mediaPlayer.currentPosition.toLong(),
                playbackSpeed
            )
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build()
        mediaSessionCompat.setPlaybackState(playBackStateNew)
    }

    override fun onMediaButtonEvent(mediaButtonEvent: Intent?): Boolean {
        val keyEvent: KeyEvent? = mediaButtonEvent?.getParcelableExtra(Intent.EXTRA_KEY_EVENT)

        if (keyEvent == null || keyEvent.action != ACTION_DOWN) {
            return false
        }

        when (keyEvent.keyCode) {
            KEYCODE_MEDIA_PLAY_PAUSE, KEYCODE_HEADSETHOOK -> {
                context.playMusic()
                return true
            }
            KEYCODE_MEDIA_NEXT -> {
                context.nextMusic()
                return true
            }
            KEYCODE_MEDIA_PLAY -> {
                context.playMusic()
                return true
            }
            KEYCODE_MEDIA_PREVIOUS -> {
                context.previousMusic()
                return true
            }

            KEYCODE_MEDIA_STOP -> {
                context.stopMusic()
                return true
            }

        }
        return false
    }

}