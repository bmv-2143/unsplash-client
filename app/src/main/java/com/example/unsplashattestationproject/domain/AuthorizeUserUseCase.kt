package com.example.unsplashattestationproject.domain

import com.example.unsplashattestationproject.data.UnsplashRepository
import javax.inject.Inject

class AuthorizeUserUseCase @Inject constructor(
    private val unsplashRepository: UnsplashRepository) {

    suspend operator fun invoke(authCode : String): Boolean {
        return unsplashRepository.getAccessToken(authCode).isNotEmpty()
    }

}