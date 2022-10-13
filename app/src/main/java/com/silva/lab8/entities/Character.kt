package com.silva.lab8.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Character (
    @PrimaryKey
    var id:Int,
    val name: String,
    val species: String,
    val status: String,
    val gender : String,
    val origin: String,
    val episodes: Int,
    val imageURL: String
)

