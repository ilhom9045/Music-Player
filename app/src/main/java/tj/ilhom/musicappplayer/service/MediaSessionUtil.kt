package tj.ilhom.musicappplayer.service

import android.content.Context
import android.os.Build
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import tj.ilhom.musicappplayer.extention.newActionIntent
import tj.ilhom.musicappplayer.service.model.MusicItem

class MediaSessionUtil(private val context: Context) {

    private companion object {
        var mediaSession: MediaSessionCompat? = null
    }

    init {
        if (mediaSession == null) {
            mediaSession = MediaSessionCompat(context, "My Music")
        }
    }

    fun mediaSession(): MediaSessionCompat {
        return mediaSession!!
    }

    fun initSession(model: MusicItem) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val player = MusicPlayerUtil()
            if (model.isPlay) {
                player.play(model.musicPath)
            }
            player.player()?.setOnCompletionListener {
                context.sendBroadcast(context.newActionIntent(NotificationUtil.NEXT))
            }

            val playbackSpeed = if (player.player()?.isPlaying == true) 1F else 0F
            mediaSession!!.setMetadata(
                MediaMetadataCompat.Builder()
                    .putLong(
                        MediaMetadataCompat.METADATA_KEY_DURATION,
                        player.player()!!.duration.toLong()
                    )
                    .build()
            )
            val playBackState = PlaybackStateCompat.Builder()
                .setState(
                    PlaybackStateCompat.STATE_PLAYING,
                    player.player()!!.currentPosition.toLong(),
                    playbackSpeed
                )
                .setActions(PlaybackStateCompat.ACTION_SEEK_TO)
                .build()
            mediaSession!!.setPlaybackState(playBackState)
            mediaSession!!.setCallback(
                MediaSessionCallBack(
                    mediaSession!!,
                    player.player()!!,
                    playbackSpeed
                )
            )
        }
    }

    fun clean() {
        mediaSession = null
    }
}