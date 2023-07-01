package com.example.unsplashattestationproject.data

import android.util.Log
import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import com.example.unsplashattestationproject.data.dto.photos.UnsplashLikeResponse
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.network.UnsplashAuthorizationService
import com.example.unsplashattestationproject.data.network.UnsplashService
import com.example.unsplashattestationproject.log.TAG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import retrofit2.HttpException
import java.net.UnknownHostException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashNetworkDataSource @Inject constructor(
    private val unsplashAuthorizationService: UnsplashAuthorizationService,
    private val unsplashService: UnsplashService
) {

    private val _networkErrorsFlow = MutableSharedFlow<NetworkErrors>()
    val networkErrorsFlow = _networkErrorsFlow.asSharedFlow()

    suspend fun getAccessToken(code: String): AuthInfo =
        unsplashAuthorizationService.unsplashAuthApi.getAccessToken(
            TokenBody(code = code)
        )

    suspend fun getPhotos(page : Int, perPage : Int) : List<UnsplashPhoto> {
        return try {
            Log.e(TAG, "\n*********** getPhotos: page = $page, perPage = $perPage ***********")
            delay(2000) // todo: REMOVE ME
            unsplashService.unsplashApi.getPhotos(
                page,
                perPage = perPage,
//                orderBy = "latest"
            )
        } catch (e: UnknownHostException) {
            _networkErrorsFlow.emit(NetworkErrors.NoInternetConnection(e.message ?: "No internet connection"))
            listOf()
        } catch (e: HttpException) {
            handleHttpException(e)
            listOf()
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotos.name} error: ${e.message}")
//            throw Exception(e)
            listOf()
        }
    }

    private suspend fun handleHttpException(e: HttpException) {
        Log.e(TAG, "${::handleHttpException.name} error: ${e.message}")
        when (e.code()) {
            403 -> _networkErrorsFlow.emit(NetworkErrors.ForbiddenApiRateExceeded(e.message()))

            401 -> _networkErrorsFlow.emit(NetworkErrors.Unauthorized(e.message()))

            else -> _networkErrorsFlow.emit(NetworkErrors.HttpError(e.message()))
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

    suspend fun likePhoto(photoId: String) : UnsplashLikeResponse {
        return try {
            unsplashService.unsplashApi.likePhoto(photoId)
        } catch (e: Exception) {
            Log.e(TAG, "${::likePhoto.name} error: ${e.message}")
            throw Exception(e) // todo: misc errors (socket timeout, 403, etc. will crash app, need to handle it)
        }
    }

    suspend fun unlikePhoto(photoId: String) : UnsplashLikeResponse {
        return try {
            unsplashService.unsplashApi.unlikePhoto(photoId)
        } catch (e: Exception) {
            Log.e(TAG, "${::likePhoto.name} error: ${e.message}")
            throw Exception(e) // todo: misc errors (socket timeout, 403, etc. will crash app, need to handle it)
        }
    }


}