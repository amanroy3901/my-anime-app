package com.avfusion.apps.myanime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anime_table")
data class AnimeEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val posterUrl: String,
    val synopsis: String?,
    val genres: String?
)

fun AnimeApiModel.toAnimeEntity(): AnimeEntity {
    return AnimeEntity(
        id = this.mal_id,
        title = this.title,
        episodes = this.episodes,
        score = this.score,
        posterUrl = this.images.jpgImage.imageUrl,
        synopsis = this.synopsis,
        genres = this.genres?.joinToString(", ") { it.name }
    )
}