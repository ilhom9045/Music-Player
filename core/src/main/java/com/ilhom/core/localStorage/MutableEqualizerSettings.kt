package com.ilhom.core.localStorage

import com.ilhom.core.localStorage.core.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface ReadEqualizerSettings : Read<EqualizerSettings?>

interface SaveEqualizerSettings : Save<EqualizerSettings>

interface MutableEqualizerSettings : SaveEqualizerSettings, ReadEqualizerSettings {

    class Base @Inject constructor(
        private val saveLocalData: SaveLocalData,
        private val readLocalData: ReadLocalData
    ) : MutableEqualizerSettings {

        override suspend fun save(data: EqualizerSettings) {
            saveLocalData.save(MUSIC_SETTINGS, data)
        }

        override suspend fun read(): Flow<EqualizerSettings?> {
            return readLocalData.readT(MUSIC_SETTINGS)
        }

        private companion object {
            const val MUSIC_SETTINGS = "MUSIC_SETTINGS"
        }

    }

}