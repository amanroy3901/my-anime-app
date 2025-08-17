package com.avfusion.apps.myanime.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.avfusion.apps.myanime.data.dao.AnimeDao
import com.avfusion.apps.myanime.data.model.AnimeEntity
import com.avfusion.apps.myanime.data.model.AnimeDetailsEntity
import com.avfusion.apps.myanime.data.model.CharacterEntity
import com.avfusion.apps.myanime.data.model.AnimeTypeConverters

@Database(entities = [AnimeEntity::class, AnimeDetailsEntity::class, CharacterEntity::class], version = 1, exportSchema = false)
@TypeConverters(AnimeTypeConverters::class)
abstract class AnimeDatabase : RoomDatabase() {

    abstract fun animeDao(): AnimeDao

    companion object {
        const val DATABASE_NAME = "anime_db"
    }
}