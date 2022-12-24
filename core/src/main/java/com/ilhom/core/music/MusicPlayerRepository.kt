package com.ilhom.core.music

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import android.media.audiofx.PresetReverb
import tj.ilhom.musicappplayer.extention.nextMusic

class MusicPlayerRepository(private val context: Context) {

    private val mediaPlayer: MediaPlayer by lazy { MediaPlayer() }

    private var playerListener: MusicPlayerListener? = null

    private val audioSesionId by lazy { mediaPlayer.audioSessionId }

    private val mEqualizer: Equalizer by lazy { Equalizer(0, audioSesionId) }
    private val presetReverb: PresetReverb by lazy { PresetReverb(0, audioSesionId) }
    private val bassBoost: BassBoost by lazy { BassBoost(0, audioSesionId) }

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