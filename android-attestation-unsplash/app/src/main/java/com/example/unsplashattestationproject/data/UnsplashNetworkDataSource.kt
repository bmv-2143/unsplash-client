package com.example.unsplashattestationproject.data

import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import com.example.unsplashattestationproject.data.network.UnsplashService
import javax.inject.Inject

class UnsplashNetworkDataSource @Inject constructor(
    private val unsplashService: UnsplashService
) {

        suspend fun getAccessToken(code: String) : AuthInfo =
            unsplashService.unsplashApi.getAccessToken(
                TokenBody(code = code)
            )

}