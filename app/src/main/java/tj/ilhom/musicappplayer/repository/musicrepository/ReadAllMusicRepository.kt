package tj.ilhom.musicappplayer.repository.musicrepository

import android.content.Context
import android.media.MediaMetadataRetriever
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO
import java.io.File
import javax.inject.Inject

interface ReadAllMusicRepository {

    fun getAllMusics(): ArrayList<MusicItemDTO>


    class Base @Inject constructor(@ApplicationContext val context: Context) : ReadAllMusicRepository {

        val musicList = ArrayList<MusicItemDTO>()

        private var id = 0

        override fun getAllMusics(): ArrayList<MusicItemDTO> {
            val directory = File("/storage/emulated/0")
            getMp3File(directory)
            return musicList
        }

        fun getMp3File(file: File) {
            Log.d("Directory", file.path + "file name = " + file.name)
            val isPrivate = file.name.startsWith(".")
            if (file.isFile && !isPrivate) {

                if (file.name.startsWith(".")) {
                    return
                }
                if (file.isFile && file.name.endsWith(".mp3")) {
                    id += 1
                    val mediaMetaData = MediaMetadataRetriever()
                    try {
                        mediaMetaData.setDataSource(file.absolutePath)
                        val duration =
                            mediaMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
                        val artistName =
                            mediaMetaData.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)
                        val dur = duration?.toLong()
                        val second = ((dur?.rem(60000))?.div(1000)).toString()
                        val minute = (dur?.div(60000)).toString()
                        val music_duration = if (second.length == 1) {
                            "0$minute:0$second"
                        } else {
                            "0$minute:$second"
                        }
                        mediaMetaData.release()
                        musicList.add(
                            MusicItemDTO(
                                id,
                                file.absolutePath,
                                music_duration,
                                file.name,
                                artistName
                            )
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            if (file.isDirectory && !isPrivate) {
                try {
                    val directoryFiles = file.listFiles()
                    directoryFiles.forEach {
                        getMp3File(it)
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}
