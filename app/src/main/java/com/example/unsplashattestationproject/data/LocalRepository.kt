package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import javax.inject.Inject

class LocalRepository  @Inject constructor(
    private val sharedPreferences: SharedPreferences
)  {

    fun isOnboardingShowed(): Boolean = sharedPreferences.getBoolean(
        IS_ONBOARDING_SHOWED, false)

    fun saveOnboardingShowedStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_ONBOARDING_SHOWED, true)
        editor.apply()
    }

    companion object {
        const val IS_ONBOARDING_SHOWED = "isOnboardingShowed"
    }
}