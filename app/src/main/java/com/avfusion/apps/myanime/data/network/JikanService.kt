package com.avfusion.apps.myanime.data.network

import com.avfusion.apps.myanime.data.model.AnimeCharactersResponse
import com.avfusion.apps.myanime.data.model.AnimeDetailsResponse
import com.avfusion.apps.myanime.data.model.TopAnimeResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface JikanService {

    @GET("top/anime")
    suspend fun getTopAnime(@Query("page") page:Int) : Response<TopAnimeResponse>

    @GET("anime/{animeId}")
    suspend fun  getAnimeDetails(@Path("animeId") animeId: Int) : Response<AnimeDetailsResponse>

    @GET("anime/{id}/characters")
    suspend fun getAnimeCharacters(@Path("id") animeId: Int): Response<AnimeCharactersResponse>
}