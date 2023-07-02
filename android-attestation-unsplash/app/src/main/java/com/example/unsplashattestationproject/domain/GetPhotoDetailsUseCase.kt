package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import javax.inject.Inject

class GetPhotoDetailsUseCase @Inject constructor(private val unsplashRepository: UnsplashRepository) {

    suspend operator fun invoke(photoId : String): UnsplashPhotoDetails? =
        unsplashRepository.getPhotoDetails(photoId)

}