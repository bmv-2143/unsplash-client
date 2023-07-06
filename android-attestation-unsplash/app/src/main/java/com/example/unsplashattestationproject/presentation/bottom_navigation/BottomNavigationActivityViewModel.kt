package com.example.unsplashattestationproject.presentation.bottom_navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavigationActivityViewModel @Inject constructor(
    application: Application,
    val unsplashRepository: UnsplashRepository
) : AndroidViewModel(application) {

    val networkErrorsFlow = unsplashRepository.networkErrorsFlow

    var selectedFromPhotoList: PhotoListItemUiModel? = null
        set(value) {
            field = value
            photoToShareId = value?.remoteId ?: ""
        }

    var photoToShareId: String = ""


    var selectedCollection: UnsplashCollection? = null

    fun getShareLink(): String =
        "https://unsplash.com/photos/${photoToShareId}"

}