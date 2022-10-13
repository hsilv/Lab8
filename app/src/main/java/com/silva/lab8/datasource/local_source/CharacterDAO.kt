package com.silva.lab8.datasource.local_source

import androidx.room.*
import com.silva.lab8.entities.Character

@Dao
interface CharacterDAO {
    @Query("SELECT * FROM character")
    suspend fun getAllCharacters(): List<Character>

    @Query("SELECT * FROM character WHERE id = :id")
    suspend fun getCharacterById(id: Int): Character

    @Insert
    suspend fun insert(character: com.silva.lab8.entities.Character)

    @Update
    suspend fun update(character: com.silva.lab8.entities.Character)

    @Delete
    suspend fun delete(character: com.silva.lab8.entities.Character): Int

    @Query("DELETE FROM character")
    suspend fun removeDB(): Int
}