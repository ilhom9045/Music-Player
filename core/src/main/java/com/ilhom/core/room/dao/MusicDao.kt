package com.ilhom.core.room.dao

import androidx.room.*
import tj.ilhom.musicappplayer.modules.main.model.MusicItemDTO
import tj.ilhom.musicappplayer.service.model.MusicItem

@Dao
interface MusicDao {

    @Query("Select * from musics")
    fun getAll(): List<MusicItemDTO>

    @Insert
    fun insert(entity: MusicItemDTO)

    @Query("Delete from musics")
    fun deleteAll()

    @Delete
    fun delete(item: MusicItemDTO)

    @Query("Select * from musics limit :start offset :end")
    fun getRangeMusic(start: Int, end: Int): List<MusicItemDTO>

    @Insert
    fun insertAll(musics: List<MusicItemDTO>)

    @Query("SELECT EXISTS (SELECT 1 FROM musics WHERE id = :id)")
    fun exist(id: Int): Boolean

    @Query("Select * from musics where id = :id")
    fun item(id: Int): MusicItemDTO

    @Query("Select * from musics order by id desc limit 1")
    fun latestItem(): MusicItemDTO

    @Query("Select * from musics limit 1")
    fun firstItem(): MusicItemDTO

    @Query("Select * from musics where id != :id  ORDER BY RANDOM() LIMIT 1")
    fun randomMusic(id: Int): MusicItemDTO

    suspend fun next(id: Int): MusicItemDTO {
        return if (exist(id + 1))
            item(id + 1)
        else
            firstItem()
    }

    suspend fun previous(id: Int): MusicItemDTO {
        return if (id == 1) {
            latestItem()
        } else if (exist(id - 1))
            item(id - 1)
        else
            firstItem()
    }
}