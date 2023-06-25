package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import android.view.View
import com.example.unsplashattestationproject.databinding.FragmentPhotoListBinding
import com.example.unsplashattestationproject.log.TAG

sealed class PhotoListFragmentState {

    abstract fun activate(binding: FragmentPhotoListBinding)

    internal fun setProgressVisibility(binding: FragmentPhotoListBinding, isVisible: Boolean) {
        binding.fragmentPhotoProgressBar.visibility = if (isVisible) View.VISIBLE else View.GONE
    }

    object FirstPageLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE First Page Loading")
            setProgressVisibility(binding, true)
        }
    }

    object FirstPageNotLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE First Page Not Loading")
            setProgressVisibility(binding, false)
        }
    }

    object FirstPageLoadError : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE First Page Load Error")
        }
    }

    object NextPageLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE Next Page Loading")
            setProgressVisibility(binding, true)
        }
    }


    object NextPageNotLoading : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE Next Page Not Loading")
            setProgressVisibility(binding, false)
        }
    }

    object NextPageLoadError : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE Next Page Load Error")
        }
    }

    object LoadingFromDb : PhotoListFragmentState() {
        override fun activate(binding: FragmentPhotoListBinding) {
            Log.e(TAG, "STATE LoadingFromDb")
        }
    }

}