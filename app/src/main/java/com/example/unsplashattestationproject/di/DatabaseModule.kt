package com.example.unsplashattestationproject.di

import android.content.Context
import androidx.room.Room
import com.example.unsplashattestationproject.BuildConfig
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Singleton
    @Provides
    fun providePhotoDao(database: PhotoDatabase) = database.photoDao()

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): PhotoDatabase {
        return Room.databaseBuilder(
            context,
            PhotoDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()
    }

}