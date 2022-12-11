package tj.ilhom.musicappplayer.repository.localStorage.core

interface Save<T> {

    suspend fun save(data: T)
}