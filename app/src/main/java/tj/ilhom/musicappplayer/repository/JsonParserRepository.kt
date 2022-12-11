package tj.ilhom.musicappplayer.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject

interface JsonParserRepository {

    fun toJson(clazz: Any?): String?

    fun <T : Any> fromJson(json: String,clazz: Class<T>): T

    class Base @Inject constructor(): JsonParserRepository {

        private val gson = Gson()

        override fun toJson(clazz: Any?): String? {
            return gson.toJson(clazz)
        }

        override fun <T : Any> fromJson(json: String,clazz: Class<T>): T {
            return gson.fromJson(json, clazz)
        }
    }
}

inline fun <reified T> JsonParserRepository.fromTJson(json: String): T =
    Gson().fromJson(json, object : TypeToken<T>() {}.type)