package tj.ilhom.musicappplayer.repository.localStorage.core

import android.content.SharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

abstract class AbstractLocalData(
    private val sharedPreferences: SharedPreferences,
) : SaveLocalData, ReadLocalData {

    private val sharedEditor: SharedPreferences.Editor = sharedPreferences.edit()

    override suspend fun saveString(key: String, value: String?) {
        sharedEditor.putString(key, value).apply()
    }

    override suspend fun saveInt(key: String, value: Int) {
        sharedEditor.putInt(key, value).apply()
    }

    override suspend fun saveLong(key: String, value: Long) {
        sharedEditor.putLong(key, value).apply()
    }

    override suspend fun saveBoolean(key: String, value: Boolean) {
        sharedEditor.putBoolean(key, value).apply()
    }

    override suspend fun saveFloat(key: String, value: Float) {
        sharedEditor.putFloat(key, value).apply()
    }

    override suspend fun readString(key: String, defaultValue: String?): Flow<String?> {
        return flow { emit(sharedPreferences.getString(key, defaultValue)) }
    }

    override suspend fun readInt(key: String, defaultValue: Int): Flow<Int> {
        return flow { emit(sharedPreferences.getInt(key, defaultValue)) }
    }

    override suspend fun readLong(key: String, defaultValue: Long): Flow<Long> {
        return flow { emit(sharedPreferences.getLong(key, defaultValue)) }
    }

    override suspend fun readBoolean(key: String, defaultValue: Boolean): Flow<Boolean> {
        return flow { emit(sharedPreferences.getBoolean(key, defaultValue)) }
    }

    override suspend fun readFloat(key: String, defaultValue: Float): Flow<Float> {
        return flow { emit(sharedPreferences.getFloat(key, defaultValue)) }
    }

    override suspend fun remove(key: String): Flow<Boolean> {
        return flow {
            try {
                sharedEditor.remove(key)
                emit(true)
            } catch (e: Exception) {
                e.printStackTrace()
                emit(false)
            }
        }
    }

    override suspend fun clear() {
        sharedEditor.clear()
    }

}