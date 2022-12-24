package com.ilhom.core.localStorage.latestMusic

import kotlinx.coroutines.flow.Flow
import com.ilhom.core.localStorage.PlayMusicConfigStorage
import com.ilhom.core.localStorage.core.Read
import com.ilhom.core.localStorage.core.Save
import com.ilhom.core.localStorage.core.readT
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