package com.ilhom.core.localStorage.core

interface SaveLocalData {

    suspend fun <T> save(key: String, data: T)

    suspend fun saveString(key: String, value: String?)

    suspend fun saveInt(key: String, value: Int)

    suspend fun saveLong(key: String, value: Long)

    suspend fun saveBoolean(key: String, value: Boolean)

    suspend fun saveFloat(key: String, value: Float)

}