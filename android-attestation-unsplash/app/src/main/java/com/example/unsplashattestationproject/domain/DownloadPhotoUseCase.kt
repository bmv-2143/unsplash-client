package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import javax.inject.Inject

class DownloadPhotoUseCase @Inject constructor(private val unsplashRepository: UnsplashRepository) {

    operator fun invoke(photoUrl : String) = unsplashRepository.downloadPhoto(photoUrl)

}