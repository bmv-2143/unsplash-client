package com.example.unsplashattestationproject.presentation.authorization

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.presentation.onboarding.OnboardingActivity

class AuthorizationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorization)

        if (!isOnboardingShowed()) {
            openOnboardingActivity()
        }
    }

    private fun openOnboardingActivity() {
        intent = Intent(this, OnboardingActivity::class.java)
        startActivity(intent)
        saveOnboardingShowedStatus()
    }

    private fun saveOnboardingShowedStatus() {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_ONBOARDING_SHOWED, true)
        editor.apply()
    }

    private fun isOnboardingShowed(): Boolean {
        val sharedPreferences = getSharedPreferences(SHARED_PREFERENCES, MODE_PRIVATE)
        return sharedPreferences.getBoolean(IS_ONBOARDING_SHOWED, false)
    }

    companion object {
        const val IS_ONBOARDING_SHOWED = "isOnboardingShowed"
        const val SHARED_PREFERENCES = "sharedPreferences"
    }
}