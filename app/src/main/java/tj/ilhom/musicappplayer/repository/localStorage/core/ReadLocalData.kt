package tj.ilhom.musicappplayer.repository.localStorage.core

import kotlinx.coroutines.flow.Flow

interface ReadLocalData {

    suspend fun <T:Any> read(key: String, clazz: Class<T>): Flow<T?>

    suspend fun readString(key: String, defaultValue: String?): Flow<String?>

    suspend fun readInt(key: String, defaultValue: Int): Flow<Int>

    suspend fun readLong(key: String, defaultValue: Long): Flow<Long>

    suspend fun readBoolean(key: String, defaultValue: Boolean): Flow<Boolean>

    suspend fun readFloat(key: String, defaultValue: Float): Flow<Float>

    suspend fun remove(key: String): Flow<Boolean>

    suspend fun clear()

}

suspend inline fun <reified T:Any> ReadLocalData.readT(key: String): Flow<T?> {
    return read(key, T::class.java)
}

suspend fun ReadLocalData.readStringOrNull(key: String): Flow<String?> {
    return readString(key, null)
}
