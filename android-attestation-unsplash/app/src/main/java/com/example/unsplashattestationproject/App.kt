package com.example.unsplashattestationproject

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        const val SHARED_PREFERENCES = "sharedPreferences"
    }
}