package com.silva.lab8.db

data class CharacterRM(
    val name: String,
    val species: String,
    val status: String,
    val gender: String,
    val image: String
)

object RickAndMortyDB {

    private val characters = mutableListOf(
        CharacterRM(
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg"
        ),
        CharacterRM(
            name = "Morty Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
        ),
        CharacterRM(
            name = "Summer Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
        ),
        CharacterRM(
            name = "Beth Smith",
            status = "Alive",
            species = "Human",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/4.jpeg",
        ),
        CharacterRM(
            name = "Jerry Smith",
            status = "Alive",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
        ),
        CharacterRM(
            name = "Abadango Cluster Princess",
            status = "Alive",
            species = "Alien",
            gender = "Female",
            image = "https://rickandmortyapi.com/api/character/avatar/6.jpeg",
        ),
        CharacterRM(
            name = "Abradolf Lincler",
            status = "unknown",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
        ),
        CharacterRM(
            name = "Adjudicator Rick",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/8.jpeg",
        ),
        CharacterRM(
            name = "Agency Director",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/9.jpeg",
        ),
        CharacterRM(
            name = "Alan Rails",
            status = "Dead",
            species = "Human",
            gender = "Male",
            image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
        ),
    )
    
    fun getCharacters() = characters

}