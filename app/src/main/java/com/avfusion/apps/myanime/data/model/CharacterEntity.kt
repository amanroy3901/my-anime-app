package com.avfusion.apps.myanime.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val malId: Int,
    val animeId: Int,
    val name: String,
    val role: String,
    @Embedded(prefix = "char_img_")
    val images: CharacterImages,
    @SerializedName("voice_actors")
    val voiceActors: List<VoiceActor>
)



fun CharacterRole.toCharacterEntity(animeId: Int): CharacterEntity {
    return CharacterEntity(
        malId = character.malId,
        animeId = animeId,
        name = character.name,
        role = role,
        images = character.images,
        voiceActors = voiceActors
    )
}
