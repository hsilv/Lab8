package com.silva.lab8.datasource.api

import com.silva.lab8.datasource.model.CharacterDTO
import com.silva.lab8.datasource.model.CharacterResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RMAPI {
    @GET("/api/character")
    fun getCharacter(): Call<CharacterResponse>

    @GET("/api/character/{id}")
    fun getCharacterById(
        @Path("id") id: String
    ): Call<CharacterDTO>

}