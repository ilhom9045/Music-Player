package tj.ilhom.musicappplayer.repository.room.database

import androidx.room.Database
import androidx.room.RoomDatabase
import tj.ilhom.musicappplayer.module.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.repository.room.dao.MusicDao

@Database(entities = [MusicItemDTO::class], version = 1, exportSchema = false)
abstract class MusicDatabase() : RoomDatabase() {
    abstract fun dao(): MusicDao
}