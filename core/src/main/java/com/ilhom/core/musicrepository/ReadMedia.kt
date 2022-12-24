package com.ilhom.core.musicrepository

import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaMetadataRetriever
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.extention.musicDrawable
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import com.ilhom.core.timer.TimerRepository
import javax.inject.Inject

interface ReadMedia {

    fun readIcon(path: String): Drawable?

    fun mediaInfo(id: Int, path: String, name: String): MusicItemDTO?

    class Base @Inject constructor(
        @ApplicationContext private val context: Context,
        private val timerRepository: TimerRepository
    ) : ReadMedia {

        override fun readIcon(path: String): Drawable? {
            return path.musicDrawable(context)
        }

        override fun mediaInfo(id: Int, path: String, name: String): MusicItemDTO? {
            val mediaMetaData = MediaMetadataRetriever()
            try {
                mediaMetaData.setDataSource(path)
                val duration =
                    mediaMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                val artistName =
                    mediaMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)

                mediaMetaData.release()
                return MusicItemDTO(
                    id,
                    path,
                    timerRepository.durationToTime(duration!!),
                    name,
                    artistName
                )
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
        }
    }
}