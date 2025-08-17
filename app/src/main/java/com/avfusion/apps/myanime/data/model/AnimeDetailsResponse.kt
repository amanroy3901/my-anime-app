package com.avfusion.apps.myanime.data.model

import com.google.gson.annotations.SerializedName

data class AnimeDetailsResponse(
  val data: AnimeDetailApiModel
)

data class AnimeDetailApiModel(
  @SerializedName("mal_id") val malId: Int,
  val url: String?,
  val title: String,
  @SerializedName("images") val images: ImagesWrapper,
  val synopsis: String?,
  val genres: List<Genre>?,
  val episodes: Int?,
  val score: Double?,
  val titles: List<Title>?,
  val trailer: Trailer?,
)

data class ImagesWrapper(
  val jpg: ImageUrls
)

data class ImageUrls(
  @SerializedName("large_image_url") val largeImageUrl: String
)

data class Title(
  val type: String?,
  val title: String?
)

data class Genre(
  val mal_id: Int,
  val name: String
)

data class Trailer(
  @SerializedName("youtube_id") val youtubeId: String?,
  @SerializedName("images") val images: TrailerImages?
)

data class TrailerImages(
  @SerializedName("maximum_image_url") val maximumImageUrl: String?
)

