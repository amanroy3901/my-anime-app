package com.avfusion.apps.myanime.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters

@Entity(tableName = "anime_details_table")
@TypeConverters(AnimeTypeConverters::class)
data class AnimeDetailsEntity(
    @PrimaryKey
    val id: Int,
    val title: String,
    val posterUrl: String,
    val synopsis: String?,
    val genres: List<Genre>?,
    val episodes: Int?,
    val score: Double?,
    val trailer: TrailerWithImageUrls?
)

data class TrailerWithImageUrls(
    val youtubeId: String?,
    val maximumImageUrl: String?
)

fun AnimeDetailApiModel.toAnimeDetailsEntity(): AnimeDetailsEntity {
    return AnimeDetailsEntity(
        id = this.malId,
        title = this.title,
        posterUrl = this.images.jpg.largeImageUrl,
        synopsis = this.synopsis,
        genres = this.genres,
        episodes = this.episodes,
        score = this.score,
        trailer = this.trailer?.let {
            TrailerWithImageUrls(
                youtubeId = it.youtubeId,
                maximumImageUrl = it.images?.maximumImageUrl
            )
        }
    )
}