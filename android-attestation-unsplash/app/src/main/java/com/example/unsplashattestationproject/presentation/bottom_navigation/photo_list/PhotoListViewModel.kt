package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.domain.GetPhotosUseCase
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(private val getPhotosUseCase: GetPhotosUseCase) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PHOTO LIST"
    }
    val text: LiveData<String> = _text


    fun getPhotos() {
        viewModelScope.launch {
            val result = getPhotosUseCase()
            Log.e(TAG, "getPhotos: $result")
            Log.e(TAG, "getPhotos: ${result.getOrNull()}")
        }
    }

}