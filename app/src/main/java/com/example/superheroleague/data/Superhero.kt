package com.example.superheroleague.data

import androidx.annotation.ColorRes
import com.example.superheroleague.R
import com.google.gson.TypeAdapter
import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

data class SuperheroSearchResponse(
    val results: List<Superhero>
)

data class Superhero(
    val id: String,
    val name: String,
    val image: Image,
    val biography: Biography,
    val work: Work,
    val appearance: Appearance,
    @SerializedName("powerstats") val stats: Stats
) {

    @ColorRes
    fun getAlignmentColor() : Int {
        return when (biography.alignment) {
            "good" -> R.color.alignment_color_good
            "bad" -> R.color.alignment_color_bad
            else -> R.color.alignment_color_neutral
        }
    }
}

data class Work(
    val occupation: String,
    val base: String
)

data class Biography(
    @SerializedName("full-name") val realName: String,
    @SerializedName("place-of-birth") val placeOfBirth: String,
    val publisher: String,
    val alignment: String
)

data class Appearance (
    val race: String,
    val gender: String,
    @SerializedName("eye-color") val eyeColor: String,
    @SerializedName("hair-color") val hairColor: String,
    val height: List<String>,
    val weight: List<String>,
)

data class Stats(
    @JsonAdapter(IntegerAdapter::class) var intelligence: Int,
    @JsonAdapter(IntegerAdapter::class) var strength: Int,
    @JsonAdapter(IntegerAdapter::class) var speed: Int,
    @JsonAdapter(IntegerAdapter::class) var durability: Int,
    @JsonAdapter(IntegerAdapter::class) var power: Int,
    @JsonAdapter(IntegerAdapter::class) var combat: Int
)

data class Image(
    val url: String
)

class IntegerAdapter : TypeAdapter<Int>() {
    override fun write(out: JsonWriter?, value: Int) {
        out?.value(value)
    }

    override fun read(`in`: JsonReader?): Int {
        return try {
            `in`!!.nextString()!!.toInt()
        } catch (e: Exception) {
            0
        }
    }

}