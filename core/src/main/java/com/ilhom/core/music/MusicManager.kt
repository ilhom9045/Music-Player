package com.ilhom.core.music

import android.content.Context
import android.content.Intent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import com.ilhom.core.localStorage.MutableMusicConfig
import com.ilhom.core.localStorage.latestMusic.SaveLatestMusic
import com.ilhom.core.room.dao.MusicDao
import javax.inject.Inject

interface MusicManager {

    fun previous(listener: MusicNotificationBroadcast.MusicNotificationListener? = MusicNotificationBroadcast.musicNotificationListener)

    fun play(listener: MusicNotificationBroadcast.MusicNotificationListener? = MusicNotificationBroadcast.musicNotificationListener)

    fun pause(listener: MusicNotificationBroadcast.MusicNotificationListener? = MusicNotificationBroadcast.musicNotificationListener)

    fun stop(listener: MusicNotificationBroadcast.MusicNotificationListener? = MusicNotificationBroadcast.musicNotificationListener)

    fun next(listener: MusicNotificationBroadcast.MusicNotificationListener? = MusicNotificationBroadcast.musicNotificationListener)

    fun exit()

    class Base @Inject constructor(
        @ApplicationContext private val context: Context,
        private val musicDao: MusicDao,
        private val coroutineScope: CoroutineScope,
        private val playerUtil: MusicPlayerRepository,
        notificationUtil: NotificationUtil,
        private val musicConfig: MutableMusicConfig,
        private val saveLatestMusic: SaveLatestMusic,
    ) : MusicManager {

        private val model = notificationUtil.notificationModel()

        override fun previous(listener: MusicNotificationBroadcast.MusicNotificationListener?) {
            coroutineScope.launch {
                val item: MusicItem? =
                    when (musicConfig.read(MutableMusicConfig.MusicConfig.Replay()).first()) {

                        is MutableMusicConfig.MusicConfig.PlayOnes -> {
                            model
                        }

                        is MutableMusicConfig.MusicConfig.Random -> {
                            model?.id?.let { it1 -> musicDao.randomMusic(it1).toMusicItem() }
                        }

                        is MutableMusicConfig.MusicConfig.Replay -> {
                            model?.id?.let { it1 -> musicDao.previous(it1).toMusicItem() }
                        }
                    }

                launch(Dispatchers.Main) {
                    item?.let { it1 ->
                        listener?.previous(it1)
                        context.startMusicService(it1)
                    }
                }
            }
        }

        override fun play(listener: MusicNotificationBroadcast.MusicNotificationListener?) {
            val isPlay = playerUtil.player().isPlaying
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

        override fun pause(listener: MusicNotificationBroadcast.MusicNotificationListener?) {
            listener?.pause()
            playerUtil.pause()
            model?.isPlay = false
            model?.let { it1 ->
                context.startMusicService(it1)
            }
        }

        override fun stop(listener: MusicNotificationBroadcast.MusicNotificationListener?) {
            listener?.pause()
            playerUtil.stop()
            model?.isPlay = false
            model?.let { it1 ->
                context.startMusicService(it1)
            }
        }

        override fun next(listener: MusicNotificationBroadcast.MusicNotificationListener?) {
            coroutineScope.launch {
                val item: MusicItem? =
                    when (musicConfig.read(MutableMusicConfig.MusicConfig.Replay()).first()) {

                        is MutableMusicConfig.MusicConfig.PlayOnes -> {
                            model
                        }

                        is MutableMusicConfig.MusicConfig.Random -> {
                            model?.id?.let { it1 -> musicDao.randomMusic(it1).toMusicItem() }
                        }

                        is MutableMusicConfig.MusicConfig.Replay -> {
                            model?.id?.let { it1 -> musicDao.next(it1).toMusicItem() }
                        }
                    }

                launch(Dispatchers.Main) {
                    item?.let { it1 ->
                        listener?.next(it1)
                        context.startMusicService(it1)
                    }
                }
            }
        }

        override fun exit() {
            runBlocking {
                val model = model?.toLatestMusicModel(playerUtil.player().currentPosition)
                model?.let { saveLatestMusic.save(it) }
            }
            playerUtil.clean()
            context.stopService(Intent(context, MusicService::class.java))
        }

    }

}