package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import javax.inject.Inject

class LikePhotoUseCase @Inject constructor(private val unsplashRepository: UnsplashRepository) {

    suspend operator fun invoke(photoId : String, isLiked : Boolean): UnsplashPhoto? =
        unsplashRepository.updateLikeStatus(photoId, isLiked)

}