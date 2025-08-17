package com.avfusion.apps.myanime.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AnimeTypeConverters {
    private val gson = Gson()

    @TypeConverter
    fun fromGenreList(genreList: List<Genre>?): String? {
        return gson.toJson(genreList)
    }

    @TypeConverter
    fun toGenreList(genreListString: String?): List<Genre>? {
        val listType = object : TypeToken<List<Genre>>() {}.type
        return gson.fromJson(genreListString, listType)
    }

    @TypeConverter
    fun fromVoiceActorList(voiceActors: List<VoiceActor>?): String? {
        return Gson().toJson(voiceActors)
    }

    @TypeConverter
    fun toVoiceActorList(voiceActorsString: String?): List<VoiceActor>? {
        val type = object : TypeToken<List<VoiceActor>>() {}.type
        return Gson().fromJson(voiceActorsString, type)
    }

    @TypeConverter
    fun fromTrailer(trailer: TrailerWithImageUrls?): String? {
        return gson.toJson(trailer)
    }

    @TypeConverter
    fun toTrailer(trailerString: String?): TrailerWithImageUrls? {
        return gson.fromJson(trailerString, TrailerWithImageUrls::class.java)
    }
}