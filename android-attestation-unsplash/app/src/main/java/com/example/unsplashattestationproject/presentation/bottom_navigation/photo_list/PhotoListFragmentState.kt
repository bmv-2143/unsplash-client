package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.view.View
import com.example.unsplashattestationproject.databinding.FragmentPhotoListBinding

sealed class PhotoListFragmentState {

    abstract fun activate(binding: FragmentPhotoListBinding)

    internal fun setProgressVisibility(binding: FragmentPhotoListBinding, isVisible: Boolean) {
        binding.fragmentPhotoProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    object FirstPageLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            setProgressVisibility(binding, true)
        }
    }

    object FirstPageNotLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            setProgressVisibility(binding, false)
        }
    }

    object NextPageLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            setProgressVisibility(binding, true)
        }
    }


    object NextPageNotLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            setProgressVisibility(binding, false)
        }
    }

}