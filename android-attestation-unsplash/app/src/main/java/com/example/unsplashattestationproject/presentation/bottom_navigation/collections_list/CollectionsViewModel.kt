package com.example.unsplashattestationproject.presentation.bottom_navigation.collections_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel
class CollectionsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "COLLECTIONS"
    }
    val text: LiveData<String> = _text
}