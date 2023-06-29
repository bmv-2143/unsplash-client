package com.example.unsplashattestationproject.presentation.bottom_navigation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BottomNavigationActivityViewModel @Inject constructor(application: Application
) : AndroidViewModel(application) {

    var selectedFromPhotoList: PhotoListItemUiModel? = null
        set(value) {
            field = value
            photoToShareId = value?.remoteId ?: ""
        }

    var photoToShareId: String = ""

    fun getShareLink(): String =
        "https://unsplash.com/photos/${photoToShareId}"

}