package com.example.unsplashattestationproject.presentation.bottom_navigation.user_profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class UserProfileViewModel @Inject constructor(): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "USER PROFILE"
    }
    val text: LiveData<String> = _text
}