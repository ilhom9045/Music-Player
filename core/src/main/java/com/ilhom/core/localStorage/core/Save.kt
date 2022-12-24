package com.ilhom.core.localStorage.core

interface Save<T> {

    suspend fun save(data: T)
}