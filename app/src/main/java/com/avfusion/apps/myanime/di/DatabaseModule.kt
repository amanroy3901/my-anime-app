package com.avfusion.apps.myanime.di

import android.content.Context
import androidx.room.Room
import com.avfusion.apps.myanime.data.dao.AnimeDao
import com.avfusion.apps.myanime.data.db.AnimeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AnimeDatabase {
        return Room.databaseBuilder(
            context,
            AnimeDatabase::class.java,
            AnimeDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideAnimeDao(database: AnimeDatabase): AnimeDao {
        return database.animeDao()
    }
}