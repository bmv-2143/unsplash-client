package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.profile.UnsplashUserProfile
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val unsplashRepository: UnsplashRepository) {

    suspend operator fun invoke(): UnsplashUserProfile? =
        unsplashRepository.getUserProfile()

}