package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.data.dto.AuthInfo
import com.example.unsplashattestationproject.data.dto.TokenBody
import retrofit2.http.Body
import retrofit2.http.POST

interface UnsplashApi {

    @POST("oauth/token")
    suspend fun getAccessToken(@Body tokenData: TokenBody): AuthInfo

}