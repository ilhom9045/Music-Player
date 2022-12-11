package tj.ilhom.musicappplayer.repository.localStorage.latestMusic

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import tj.ilhom.musicappplayer.extention.await
import tj.ilhom.musicappplayer.repository.localStorage.PlayMusicConfigStorage
import tj.ilhom.musicappplayer.repository.localStorage.core.Read
import tj.ilhom.musicappplayer.repository.localStorage.core.Save
import tj.ilhom.musicappplayer.repository.localStorage.core.readT
import javax.inject.Inject

interface ReadLatestMusic : Read<LatestMusicModel?>
interface SaveLatestMusic : Save<LatestMusicModel>

interface MutableLatestMusic : SaveLatestMusic, ReadLatestMusic {

    class Base @Inject constructor(private val musicConfigStorage: PlayMusicConfigStorage) :
        MutableLatestMusic {

        override suspend fun save(data: LatestMusicModel) {
            musicConfigStorage.save(latestMusic, data)
        }

        override suspend fun read(): Flow<LatestMusicModel?> {
            return musicConfigStorage.readT(latestMusic)
        }

        private companion object {
            const val latestMusic = "latestMusic"
        }

    }
}