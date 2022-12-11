package tj.ilhom.musicappplayer.repository.localStorage

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import tj.ilhom.musicappplayer.repository.localStorage.MutableMusicConfig.MusicConfig.Companion.PlayOnesCode
import tj.ilhom.musicappplayer.repository.localStorage.MutableMusicConfig.MusicConfig.Companion.RandomCode
import tj.ilhom.musicappplayer.repository.localStorage.MutableMusicConfig.MusicConfig.Companion.ReplayCode
import tj.ilhom.musicappplayer.repository.localStorage.core.ReadDefaultValue
import tj.ilhom.musicappplayer.repository.localStorage.core.Save
import javax.inject.Inject

interface MutableMusicConfig : ReadDefaultValue<MutableMusicConfig.MusicConfig>,
    Save<MutableMusicConfig.MusicConfig> {

    class Base @Inject constructor(private val playMusicConfigStorage: PlayMusicConfigStorage) :
        MutableMusicConfig {

        override suspend fun read(defaultValue: MusicConfig): Flow<MusicConfig> {
            return flow {

                val config = playMusicConfigStorage.readInt(
                    MutableMusicConfigName,
                    defaultValue.playConfig()
                ).first()

                if (config == defaultValue.playConfig()) {
                    emit(defaultValue)
                    return@flow
                }

                when (config) {
                    PlayOnesCode -> {
                        emit(MusicConfig.PlayOnes())
                    }
                    ReplayCode -> {
                        emit(MusicConfig.Replay())
                    }
                    RandomCode -> {
                        emit(MusicConfig.Random())
                    }

                }
            }
        }

        override suspend fun save(data: MusicConfig) {
            playMusicConfigStorage.saveInt(MutableMusicConfigName, data.playConfig())
        }

        private companion object {

            const val MutableMusicConfigName = "mutable_music_config_name"

        }

    }

    sealed class MusicConfig {

        abstract fun playConfig(): Int

        class PlayOnes : MusicConfig() {

            override fun playConfig(): Int = PlayOnesCode
        }

        class Replay : MusicConfig() {

            override fun playConfig(): Int = ReplayCode

        }

        class Random : MusicConfig() {
            override fun playConfig(): Int = RandomCode
        }

        companion object {
            internal const val PlayOnesCode = 1
            internal const val ReplayCode = 2
            internal const val RandomCode = 3
        }

    }
}