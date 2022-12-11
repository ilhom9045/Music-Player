package tj.ilhom.musicappplayer.service

import android.content.Context
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import tj.ilhom.musicappplayer.service.model.MusicItem

class MediaSessionUtil(
    private val context: Context,
    private val player: MusicPlayerUtil
) : MusicPlayerUtil.MusicPlayerListener {

    private val mediaSession: MediaSessionCompat by lazy {
        MediaSessionCompat(context, "My Music")
    }

    private val callBack by lazy {
        MediaSessionCallBack(
            context,
            mediaSession,
            player.player(),
        )
    }

    init {
        mediaSession.setFlags(
            MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                    MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        )
        player.setListener(this)
    }

    fun mediaSession(): MediaSessionCompat {
        return mediaSession
    }

    fun initSession(model: MusicItem) {
        if (model.isPlay) {
            player.play(model.musicPath)
        }
        onPlay()
    }

    override fun onPlay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val playbackSpeed = if (player.player()?.isPlaying == true) 1F else 0F
            mediaSession.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        player.player().duration.toLong()
                    )
                    .build()
            )
            val playBackState = PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    player.player().currentPosition.toLong(),
                    playbackSpeed
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession.setPlaybackState(playBackState)
            mediaSession.setCallback(callBack)
        }
    }

    override fun onSeekTo(pos: Long) {
        callBack.onSeekTo(pos)
    }
}