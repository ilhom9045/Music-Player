package tj.ilhom.musicappplayer.service

import android.media.MediaPlayer
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat

class MediaSessionCallBack(
    private val mediaSessionCompat: MediaSessionCompat,
    private val mediaPlayer: MediaPlayer,
    private val playbackSpeed: Float
) : MediaSessionCompat.Callback() {

    override fun onSeekTo(pos: Long) {
        super.onSeekTo(pos)
        mediaPlayer!!.seekTo(pos.toInt())
        val playBackStateNew = PlaybackStateCompat.Builder()
            .setState(
                PlaybackStateCompat.STATE_PLAYING,
                mediaPlayer!!.currentPosition.toLong(),
                playbackSpeed
            )
            .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
            .build()
        mediaSessionCompat.setPlaybackState(playBackStateNew)
    }
}