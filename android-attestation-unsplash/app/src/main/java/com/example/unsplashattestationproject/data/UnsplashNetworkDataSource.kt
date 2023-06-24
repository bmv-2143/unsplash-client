package com.example.unsplashattestationproject.data

import android.util.Log
import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.network.UnsplashAuthorizationService
import com.example.unsplashattestationproject.data.network.UnsplashService
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject

class UnsplashNetworkDataSource @Inject constructor(
    private val unsplashAuthorizationService: UnsplashAuthorizationService,
    private val unsplashService: UnsplashService
) {

    suspend fun getAccessToken(code: String): AuthInfo =
        unsplashAuthorizationService.unsplashAuthApi.getAccessToken(
            TokenBody(code = code)
        )

    suspend fun getPhotos(page : Int) : List<UnsplashPhoto> {
        return try {
            unsplashService.unsplashApi.getPhotos(
                page,
                perPage = 10,
                orderBy = "latest"
            )
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotos.name} error: ${e.message}")
            throw Exception(e)
        }

    }

}