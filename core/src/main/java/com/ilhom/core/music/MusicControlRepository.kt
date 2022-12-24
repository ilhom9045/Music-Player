package com.ilhom.core.music

import javax.inject.Inject

interface MusicControlRepository {

    fun pause()

    fun play()

    fun next()

    fun exit()


    class Base @Inject constructor():MusicControlRepository {

        override fun pause() {

        }

        override fun play() {

        }

        override fun next() {

        }

        override fun exit() {

        }

    }
}