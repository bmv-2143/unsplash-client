package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import com.example.unsplashattestationproject.presentation.authorization.AuthorizationActivity
import javax.inject.Inject

class LocalRepository  @Inject constructor(
    private val sharedPreferences: SharedPreferences
)  {

    fun isOnboardingShowed(): Boolean = sharedPreferences.getBoolean(
        AuthorizationActivity.IS_ONBOARDING_SHOWED, false)

    fun saveOnboardingShowedStatus() {
        val editor = sharedPreferences.edit()
        editor.putBoolean(AuthorizationActivity.IS_ONBOARDING_SHOWED, true)
        editor.apply()
    }

}