package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UnsplashApi {

    @POST("oauth/token")
    suspend fun getAccessToken(@Body tokenData: TokenBody): AuthInfo

}