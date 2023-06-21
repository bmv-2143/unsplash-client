package com.example.unsplashattestationproject.presentation.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.unsplashattestationproject.R

class OnboardingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        addFragment()
    }

    private fun addFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.activity_onboarding_fragment_container, OnboardingCreateFragment())
            .commit()
    }
}