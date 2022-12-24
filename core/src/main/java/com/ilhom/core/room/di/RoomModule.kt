package com.ilhom.core.room.di

import android.content.Context
import androidx.room.Room
import com.ilhom.core.room.dao.MusicDao
import com.ilhom.core.room.database.MusicDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {

    @Provides
    @Singleton
    fun provideMusicDatabase(@ApplicationContext context: Context): MusicDatabase {
        val dbName = "musics"
        return Room.databaseBuilder(
            context,
            MusicDatabase::class.java,
            dbName
        ).build()
    }

    @Provides
    @Singleton
    fun provideMusicDao(musicDatabase: MusicDatabase): MusicDao {
        return musicDatabase.dao()
    }
}