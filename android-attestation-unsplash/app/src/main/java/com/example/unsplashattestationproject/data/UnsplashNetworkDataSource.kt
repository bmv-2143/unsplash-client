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

    private val _networkErrorFlow = MutableSharedFlow<NetworkError>()
    val networkErrorsFlow = _networkErrorFlow.asSharedFlow()

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
            )
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            listOf()
        } catch (e: HttpException) {
            handleHttpException(e)
            listOf()
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotos.name} error: ${e.message}")
            listOf()
        }
    }

    private suspend fun handleUnknownHostError(e: UnknownHostException) {
        Log.e(TAG, "${::handleUnknownHostError.name} error: ${e.message}")
        _networkErrorFlow.emit(
            NetworkError.NoInternetConnection(
                e.message ?: "No internet connection"
            )
        )
    }

    private suspend fun handleHttpException(e: HttpException) {
        Log.e(TAG, "${::handleHttpException.name} error: ${e.message}")
        when (e.code()) {
            403 -> _networkErrorFlow.emit(NetworkError.ForbiddenApiRateExceeded(e.message()))

            401 -> _networkErrorFlow.emit(NetworkError.Unauthorized(e.message()))

            else -> _networkErrorFlow.emit(NetworkError.HttpError(e.message()))
        }
    }

    suspend fun getPhotoDetails(photoId: String): UnsplashPhotoDetails? {
        return try {
            unsplashService.unsplashApi.getPhotoDetails(photoId)
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotoDetails.name} error: ${e.message}")
            null
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

    suspend fun likePhoto(photoId: String) : UnsplashLikeResponse? {
        return try {
            unsplashService.unsplashApi.likePhoto(photoId)
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "${::likePhoto.name} error: ${e.message}")
            null
        }
    }

    suspend fun unlikePhoto(photoId: String) : UnsplashLikeResponse? {
        return try {
            unsplashService.unsplashApi.unlikePhoto(photoId)
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e)
            null
        } catch (e: Exception) {
            Log.e(TAG, "${::unlikePhoto.name} error: ${e.message}")
            null
        }
    }

}