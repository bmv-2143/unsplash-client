package com.example.unsplashattestationproject.data

import android.util.Log
import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.network.UnsplashAuthorizationService
import com.example.unsplashattestationproject.data.network.UnsplashService
import com.example.unsplashattestationproject.log.TAG
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashNetworkDataSource @Inject constructor(
    private val unsplashAuthorizationService: UnsplashAuthorizationService,
    private val unsplashService: UnsplashService
) {

    suspend fun getAccessToken(code: String): AuthInfo =
        unsplashAuthorizationService.unsplashAuthApi.getAccessToken(
            TokenBody(code = code)
        )

    suspend fun getPhotos(page : Int, perPage : Int) : List<UnsplashPhoto> {
        return try {
            Log.e(TAG, "\n*********** getPhotos: page = $page, perPage = $perPage ***********")
            delay(2000)
            unsplashService.unsplashApi.getPhotos(
                page,
                perPage = perPage,
//                orderBy = "latest"
            )
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotos.name} error: ${e.message}")
            throw Exception(e)
        }
    }

    suspend fun getPhotoDetails(photoId: String): UnsplashPhotoDetails {
        return try {
            unsplashService.unsplashApi.getPhotoDetails(photoId)
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotoDetails.name} error: ${e.message}")
            throw Exception(e) // todo: misc errors (socket timeout, 403, etc. will crash app, need to handle it)
        }
    }

    suspend fun getTrackedDownloadPhotoUrl(photoId: String) : String {
        return try {
            val result = unsplashService.unsplashApi.getTrackedDownloadPhoto(photoId)
            Log.e(TAG, "downloadPhoto: photoId = $result")
            result.url
        } catch (e: Exception) {
            Log.e(TAG, "${::getTrackedDownloadPhotoUrl.name} error: ${e.message}")
            throw Exception(e) // todo: misc errors (socket timeout, 403, etc. will crash app, need to handle it)
        }
    }

}