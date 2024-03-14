package com.example.unsplashattestationproject.presentation.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnboardingAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = NUMBER_OF_FRAGMENTS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            CREATE_FRAGMENT_POSITION -> OnboardingCreateFragment.newInstance()
            SHARE_FRAGMENT_POSITION -> OnboardingShareFragment.newInstance()
            else -> OnboardingUploadFragment.newInstance()
        }
    }

    companion object {
        const val NUMBER_OF_FRAGMENTS = 3
        const val CREATE_FRAGMENT_POSITION = 0
        const val SHARE_FRAGMENT_POSITION = 1
    }
}