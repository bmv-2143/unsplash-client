package com.example.unsplashattestationproject.presentation.onboarding

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.unsplashattestationproject.databinding.ActivityOnboardingBinding
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewPagerAndAdapter()
        linkTabLayoutWithViewPager2()
        setLeftArrowButtonListener()
        setRightArrowButtonListener()
    }

    private fun setupViewPagerAndAdapter() {
        onboardingAdapter = OnboardingAdapter(this)
        binding.viewPager.adapter = onboardingAdapter
    }

    private fun linkTabLayoutWithViewPager2() {
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { _, _ -> }
            .attach()
    }

    private fun setLeftArrowButtonListener() {
        binding.leftArrow.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem > 0) {
                binding.viewPager.currentItem = currentItem - 1
            }
        }
    }

    private fun setRightArrowButtonListener() {
        binding.rightArrow.setOnClickListener {
            val currentItem = binding.viewPager.currentItem
            if (currentItem < onboardingAdapter.itemCount - 1) {
                binding.viewPager.currentItem = currentItem + 1
            }
        }
    }
}
