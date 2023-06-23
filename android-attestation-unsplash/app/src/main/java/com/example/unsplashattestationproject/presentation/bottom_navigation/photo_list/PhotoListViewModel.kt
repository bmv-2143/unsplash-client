package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class PhotoListViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PHOTO LIST"
    }
    val text: LiveData<String> = _text
}