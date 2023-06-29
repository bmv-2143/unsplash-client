package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.domain.GetPhotoDetailsUseCase
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsFragmentViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase
) : ViewModel() {

    private val _photoDetailsFlow : MutableSharedFlow<UnsplashPhotoDetails> = MutableSharedFlow()
    internal val photoDetailsFlow = _photoDetailsFlow.asSharedFlow()

    fun loadPhotoDetails(photoId: String) {
        viewModelScope.launch {
            val photoDetails = getPhotoDetailsUseCase(photoId)
            _photoDetailsFlow.emit(photoDetails)
        }
    }

    fun getShareLink(photo : PhotoListItemUiModel): String =
        "https://unsplash.com/photos/${photo.remoteId}"

}
