package com.example.unsplashattestationproject.presentation.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.unsplashattestationproject.databinding.FragmentOnboardingUploadBinding

class OnboardingUploadFragment : Fragment() {
    private var _binding: FragmentOnboardingUploadBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnboardingUploadBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {

        @JvmStatic
        fun newInstance() = OnboardingUploadFragment()
    }
}