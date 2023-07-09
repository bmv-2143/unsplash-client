package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(private val unsplashRepository: UnsplashRepository) {

    operator fun invoke(photo: UnsplashPhotoDetails) =
        unsplashRepository.startTrackedDownload(photo)

}