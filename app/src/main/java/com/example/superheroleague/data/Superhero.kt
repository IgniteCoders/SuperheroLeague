package com.example.superheroleague.data

data class SuperheroSearchResponse(
    val results: List<Superhero>
)

data class Superhero(
    val id: String,
    val name: String,
    val image: Image
)

data class Image(
    val url: String
)