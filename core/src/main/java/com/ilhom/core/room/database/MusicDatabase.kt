package com.ilhom.core.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import com.ilhom.core.room.dao.MusicDao

@Database(entities = [MusicItemDTO::class], version = 1, exportSchema = false)
abstract class MusicDatabase() : RoomDatabase() {
    abstract fun dao(): MusicDao
}