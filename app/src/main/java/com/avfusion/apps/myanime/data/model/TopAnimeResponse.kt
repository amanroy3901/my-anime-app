package com.avfusion.apps.myanime.data.model

import com.google.gson.annotations.SerializedName

data class TopAnimeResponse(
    val data: List<AnimeApiModel>
)

data class AnimeApiModel(
    val mal_id: Int,
    val title: String,
    val episodes: Int?,
    val score: Double?,
    val synopsis: String?,
    val genres: List<Genre>?,
    val images: Images
)

data class Images(
    @SerializedName("jpg") val jpgImage: JpgImage
)

data class JpgImage(
    @SerializedName("image_url") val imageUrl: String
)

