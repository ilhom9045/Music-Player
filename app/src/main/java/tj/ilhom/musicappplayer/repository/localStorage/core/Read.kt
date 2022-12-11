package tj.ilhom.musicappplayer.repository.localStorage.core

import kotlinx.coroutines.flow.Flow

interface Read<T> {

    suspend fun read(): Flow<T>
}