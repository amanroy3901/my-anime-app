package com.avfusion.apps.myanime.data.dao

import androidx.room.*
import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import kotlinx.coroutines.flow.Flow
import com.avfusion.apps.myanime.data.model.AnimeEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity

@Dao
interface AnimeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(anime: AnimeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(animeList: List<AnimeEntity>)

    @Query("SELECT * FROM anime_table ORDER BY score DESC")
    fun getAllAnime(): Flow<List<AnimeEntity>>

    @Query("SELECT * FROM anime_table WHERE id = :animeId")
    suspend fun getAnimeById(animeId: Int): AnimeEntity?

    @Query("DELETE FROM anime_table")
    suspend fun deleteAllAnime()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAnimeDetails(animeDetails: AnimeDetailsEntity)

    @Query("SELECT * FROM anime_details_table WHERE id = :animeId")
    fun getAnimeDetails(animeId: Int): Flow<AnimeDetailsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCharacters(characters: List<CharacterEntity>)

    @Query("SELECT * FROM characters WHERE animeId = :animeId")
    fun getCharactersForAnime(animeId: Int): Flow<List<CharacterEntity>>
}