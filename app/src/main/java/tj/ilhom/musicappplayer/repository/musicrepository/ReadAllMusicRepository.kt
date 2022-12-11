package tj.ilhom.musicappplayer.repository.musicrepository

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import java.io.File
import javax.inject.Inject

interface ReadAllMusicRepository {

    fun getAllMusics(): ArrayList<MusicItemDTO>

    class Base @Inject constructor(
        @ApplicationContext val context: Context,
        private val readMedia: ReadMedia
    ) : ReadAllMusicRepository {

        val musicList = ArrayList<MusicItemDTO>()

        private var id = 0

        override fun getAllMusics(): ArrayList<MusicItemDTO> {
            val directory = File("/storage/emulated/0")
            getMp3File(directory)
            return musicList
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
