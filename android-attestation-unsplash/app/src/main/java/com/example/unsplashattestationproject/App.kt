package com.example.unsplashattestationproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

const val FEATURE_FLAG_REMOTE_MEDIATOR = false

@HiltAndroidApp
class App : Application() {

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
    }
}