package tj.ilhom.musicappplayer.repository.localStorage.core

import kotlinx.coroutines.flow.Flow

interface ReadDefaultValue<T> {

    suspend fun read(defaultValue: T): Flow<T>
}