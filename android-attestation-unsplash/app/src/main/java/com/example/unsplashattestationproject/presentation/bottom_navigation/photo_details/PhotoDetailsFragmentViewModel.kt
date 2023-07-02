package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.domain.DownloadPhotoUseCase
import com.example.unsplashattestationproject.domain.GetPhotoDetailsUseCase
import com.example.unsplashattestationproject.domain.LikePhotoUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.NullPointerException
import javax.inject.Inject


@HiltViewModel
class PhotoDetailsFragmentViewModel @Inject constructor(
    private val getPhotoDetailsUseCase: GetPhotoDetailsUseCase,
    private val downloadPhotoUseCase: DownloadPhotoUseCase,
    private val likePhotoUseCase: LikePhotoUseCase
) : ViewModel() {

    private val _photoDetailsFlow: MutableSharedFlow<UnsplashPhotoDetails> = MutableSharedFlow()
    internal val photoDetailsFlow = _photoDetailsFlow.asSharedFlow()

    private var photoToDownload: UnsplashPhotoDetails? = null
    private lateinit var likesStatus: Pair<Boolean, Int>

    fun loadPhotoDetails(photoId: String) {
        viewModelScope.launch {
            getPhotoDetailsUseCase(photoId)?.let { photoDetails ->
                photoToDownload = photoDetails
                likesStatus = Pair(photoDetails.likedByUser, photoDetails.likes)
                _photoDetailsFlow.emit(photoDetails)
            }
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

    // This is here for demo purposes only (Unsplash service recommends using tracked downloads).
    fun downloadPhotoTracked() {
        viewModelScope.launch {
            val photo = photoToDownload
            val id = photo?.id ?: return@launch
            val url = downloadPhotoUseCase.getTrackedDownloadUrl(id) ?: return@launch
            downloadPhotoUseCase.startTrackedDownload(url, photo.id)
            Log.e(TAG, "requestPhotoDownloadTrackApi: url: $url")
            downloadPhotoUseCase(photo)

        }
    }

    private val _photoLikes: MutableSharedFlow<Pair<Boolean, Int>> = MutableSharedFlow()
    internal val photoLikesFlow = _photoLikes.asSharedFlow()

    fun updatePhotoLikeStatus() {
        viewModelScope.launch {
            val photo = photoToDownload
            if (photo == null) {
                Log.e(TAG, "updatePhotoLikeStatus: photoToDownload is null")
                return@launch
            }

            likePhotoUseCase(photo.id, !likesStatus.first)?.let { likeActionResult ->
                Log.e(
                    TAG,
                    "likePhoto: liked?: ${likeActionResult.likedByUser}, " +
                            "likes: ${likeActionResult.likes}"
                )
                likesStatus = Pair(likeActionResult.likedByUser, likeActionResult.likes)
                _photoLikes.emit(likesStatus)
            }
        }
    }
}
