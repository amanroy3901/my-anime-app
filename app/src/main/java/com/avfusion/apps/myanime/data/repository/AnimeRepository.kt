package com.avfusion.apps.myanime.data.repository

import android.util.Log
import com.avfusion.apps.myanime.data.dao.AnimeDao
import com.avfusion.apps.myanime.data.model.AnimeEntity
import com.avfusion.apps.myanime.data.model.toAnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.toAnimeEntity
import com.avfusion.apps.myanime.data.network.JikanService
import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity
import com.avfusion.apps.myanime.data.model.toCharacterEntity
import com.avfusion.apps.myanime.utility.NetworkMonitor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnimeRepository @Inject constructor(
    private val apiService: JikanService,
    private val localDb: AnimeDao,
    private val networkMonitor: NetworkMonitor
) {

    fun getAnimeFlow(): Flow<List<AnimeEntity>> {
        return localDb.getAllAnime()
    }

    fun getAnimeDetails(id: Int): Flow<AnimeDetailsEntity?> {
        return localDb.getAnimeDetails(id)
    }

    suspend fun fetchAndSyncAnime(page: Int) {
        if (networkMonitor.isOnline.first()) {
            try {
                val response = apiService.getTopAnime(page)

                if (response.isSuccessful) {
                    val newAnimeList = response.body()?.data ?: emptyList()
                    val entitiesToInsert = newAnimeList.map { it.toAnimeEntity() }
                    localDb.insertAll(entitiesToInsert)
                } else {
                    Log.e("AnimeRepository", "API Error: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AnimeRepository", "Network Error: ${e.message}")
            }
        }
    }

    suspend fun fetchAndSyncAnimeDetails(animeId: Int) {
        if (networkMonitor.isOnline.first()) {
            try {
                val networkResponse = apiService.getAnimeDetails(animeId)
                if (networkResponse.isSuccessful) {
                    val animeDetails = networkResponse.body()?.data
                    animeDetails?.let {
                        localDb.insertAnimeDetails(it.toAnimeDetailsEntity())
                    }
                } else {
                    Log.e("AnimeRepository", "API Error: ${networkResponse.code()}")
                }
            } catch (e: Exception) {
                Log.e("AnimeRepository", "Network Error: ${e.message}")
            }
        }
    }

    fun getAnimeCharacters(id: Int): Flow<List<CharacterEntity>> {
        return localDb.getCharactersForAnime(id)
    }

    suspend fun fetchAndSyncAnimeCharacters(animeId: Int) {
        if (networkMonitor.isOnline.first()) {
            try {
                val response = apiService.getAnimeCharacters(animeId)
                if (response.isSuccessful) {
                    val allCharacters = response.body()?.data ?: emptyList()
                    val mainJapaneseCast = allCharacters
                        .filter { it.role == "Main" }
                        .mapNotNull { characterRole ->
                            val japaneseVoiceActor = characterRole.voiceActors.find { it.language == "Japanese" }
                            if (japaneseVoiceActor != null) {
                                characterRole.toCharacterEntity(animeId)
                            } else {
                                null
                            }
                        }
                    localDb.insertCharacters(mainJapaneseCast)
                } else {
                    Log.e("AnimeRepository", "API response not successful: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AnimeRepository", "Error fetching characters: ${e.message}")
            }
        }
    }
}