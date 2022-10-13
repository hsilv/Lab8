package com.silva.lab8.datasource.local_source

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.silva.lab8.entities.Character

@Database(entities = [Character::class], version = 1)
abstract class Database: RoomDatabase() {
    abstract fun characterDao(): CharacterDAO
}