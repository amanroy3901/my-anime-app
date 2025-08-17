package com.avfusion.apps.myanime.data.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName

data class AnimeCharactersResponse(
    @SerializedName("data")
    val data: List<CharacterRole>
)

data class CharacterRole(
    @SerializedName("character")
    val character: Character,
    @SerializedName("role")
    val role: String,
    @SerializedName("voice_actors")
    val voiceActors: List<VoiceActor>
)

data class Character(
    @SerializedName("mal_id")
    val malId: Int,
    @SerializedName("images")
    val images: CharacterImages,
    @SerializedName("name")
    val name: String
)

data class CharacterImages(
    @Embedded(prefix = "jpg_")
    @SerializedName("jpg")
    val jpg: ImageJpg
)

data class ImageJpg(
    @SerializedName("image_url")
    val imageUrl: String
)

data class VoiceActor(
    @SerializedName("person")
    val person: Person,
    @SerializedName("language")
    val language: String
)

data class Person(
    @SerializedName("images")
    val images: PersonImages,
    @SerializedName("name")
    val name: String
)

data class PersonImages(
    @Embedded(prefix = "jpg_")
    @SerializedName("jpg")
    val jpg: ImageJpg
)