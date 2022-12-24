package com.ilhom.core.musicrepository

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import com.ilhom.core.timer.TimerRepository
import java.io.File
import javax.inject.Inject

interface ReadAllMusicRepository {

    fun getAllMusics(): ArrayList<MusicItemDTO>

    class Base @Inject constructor(
        @ApplicationContext val context: Context,
        private val readMedia: ReadMedia,
        private val timerRepository: TimerRepository
    ) : ReadAllMusicRepository {

        val musicList = ArrayList<MusicItemDTO>()

        private var id = 0

        @SuppressLint("Range")
        override fun getAllMusics(): ArrayList<MusicItemDTO> {
            val tempList = ArrayList<MusicItemDTO>()
            val selection = MediaStore.Audio.Media.IS_MUSIC + " != 0"
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATE_ADDED,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
            )
            val cursor = context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null,
                null, null
            )
            if (cursor != null) {
                if (cursor.moveToFirst())
                    do {
                        id++
                        val titleC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                                ?: "Unknown"
                        val idC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                                ?: "Unknown"
                        val albumC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                                ?: "Unknown"
                        val artistC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                                ?: "Unknown"
                        val pathC =
                            cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                        val durationC =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                        val albumIdC =
                            cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                                .toString()
                        val uri = Uri.parse("content://media/external/audio/albumart")

                        val artUriC = Uri.withAppendedPath(uri, albumIdC).toString()

                        val music = MusicItemDTO(
                            id = id,
                            name = titleC,
                            artist = artistC,
                            path = pathC,
                            duration = timerRepository.durationToTime(durationC.toString()),
                        )
                        val file = File(music.path)
                        if (file.exists())
                            tempList.add(music)
                    } while (cursor.moveToNext())
                cursor.close()
            }
            return tempList
        }

        fun getMp3File(file: File) {
            val isPrivate = file.name.startsWith(".")
            if (file.isFile && !isPrivate) {

                if (file.name.startsWith(".")) {
                    return
                }
                if (file.isFile && file.name.endsWith(".mp3")) {
                    id += 1
                    readMedia.mediaInfo(id, file.absolutePath, file.name)?.let { musicList.add(it) }
                }
            }
            if (file.isDirectory && !isPrivate) {
                try {
                    val directoryFiles = file.listFiles()
                    directoryFiles?.forEach {
                        getMp3File(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
