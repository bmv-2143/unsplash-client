package com.example.unsplashattestationproject

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import dagger.hilt.android.HiltAndroidApp

const val FEATURE_FLAG_REMOTE_MEDIATOR = true

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun createNotificationChannel() {
        val channel = NotificationChannel(
            UNSPLASH_APP_CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
        }

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
        const val INTENT_KEY_PHOTO_ID = "photoId"
        const val INTENT_FILTER_DATA_HOST_UNSPLASH = "unsplash.com"
        const val INTENT_FILTER_DATA_HOST_AUTH = "auth"

        const val UNSPLASH_APP_CHANNEL_ID = "UnsplashAppChannelId"
        const val CHANNEL_NAME = "Unsplash App Attestation Project Channel"
        const val CHANNEL_DESCRIPTION = "This is a channel description"
    }
}