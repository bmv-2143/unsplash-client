package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.domain.DownloadPhotoUseCase
import com.example.unsplashattestationproject.domain.GetPhotoDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsFragmentViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val downloadPhotoUseCase: DownloadPhotoUseCase
) : ViewModel() {

    private val _photoDetailsFlow: MutableSharedFlow<UnsplashPhotoDetails> = MutableSharedFlow()
    internal val photoDetailsFlow = _photoDetailsFlow.asSharedFlow()

    private var photoToDownload: UnsplashPhotoDetails? = null

    fun loadPhotoDetails(photoId: String) {
        viewModelScope.launch {
            val photoDetails = getPhotoDetailsUseCase(photoId)
            photoToDownload = photoDetails
            _photoDetailsFlow.emit(photoDetails)
        }
    }

    fun downloadPhotoRaw() {
        Log.e(TAG, "downloadPhoto: id ${photoToDownload?.id} url: ${photoToDownload?.urls?.raw}")

        if (photoToDownload != null) {
            downloadPhotoUseCase(photoToDownload!!)
        } else {
            Log.e(TAG, "Failed to start download: photoToDownload is null")
        }
    }

    // This is here for demo purposes only (Unsplash service recommends using tracked downloads)
    private fun downloadPhotoTracked() {
        viewModelScope.launch {
            val id = photoToDownload?.id
            val url = downloadPhotoUseCase.getTrackedDownloadUrl(id!!)
            downloadPhotoUseCase.startTrackedDownload(url, photoToDownload!!.id)
            Log.e(TAG, "requestPhotoDownloadTrackApi: url: $url")
            downloadPhotoUseCase(photoToDownload!!)
        }
    }
}
