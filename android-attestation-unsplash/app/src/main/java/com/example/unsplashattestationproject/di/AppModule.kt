package com.example.unsplashattestationproject.di

import android.app.DownloadManager
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.example.unsplashattestationproject.App
import com.example.unsplashattestationproject.data.downloads.Downloader
import com.example.unsplashattestationproject.data.downloads.UnsplashDownloader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Provides
    fun provideDownloadManager(@ApplicationContext context: Context): DownloadManager {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getSystemService(DownloadManager::class.java)
        } else {
            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        }
    }

    @Singleton
    @Provides
    fun provideDownloader(downloadManager: DownloadManager) : Downloader {
        return UnsplashDownloader(downloadManager)
    }

}