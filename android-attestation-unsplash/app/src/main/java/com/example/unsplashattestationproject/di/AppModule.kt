package com.example.unsplashattestationproject.di

import android.content.Context
import android.content.SharedPreferences
import com.example.unsplashattestationproject.App
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(App.SHARED_PREFERENCES, Context.MODE_PRIVATE)
    }

}