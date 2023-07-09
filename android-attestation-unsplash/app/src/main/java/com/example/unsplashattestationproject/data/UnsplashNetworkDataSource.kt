package com.example.unsplashattestationproject.data

import android.util.Log
import com.example.unsplashattestationproject.data.dto.auth.AuthInfo
import com.example.unsplashattestationproject.data.dto.auth.TokenBody
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.data.dto.photos.UnsplashLikeResponse
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.dto.profile.UnsplashUserProfile
import com.example.unsplashattestationproject.data.network.UnsplashAuthorizationService
import com.example.unsplashattestationproject.data.network.UnsplashService
import com.example.unsplashattestationproject.log.TAG
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
            logError(::getPhotos.name, e)
            listOf()
        }
    }

    private suspend fun handleUnknownHostError(e: UnknownHostException) {
        logError(::handleUnknownHostError.name, e)
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
            logError(::getPhotoDetails.name, e)
            null
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
            logError(::likePhoto.name, e)
            null
        }
    }

    private fun logError(methodName: String, e: Exception) {
        Log.e(TAG, "$methodName error: ${e.message}")
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
            logError(::unlikePhoto.name, e)
            null
        }
    }

    suspend fun search(query : String, page: Int, perPage: Int) : List<UnsplashPhoto> {
        return try {
            unsplashService.unsplashApi.searchPhotos(query, page, perPage).results
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            emptyList()
        } catch (e: HttpException) {
            handleHttpException(e)
            emptyList()
        } catch (e: Exception) {
            logError(::search.name, e)
            emptyList()
        }
    }

    suspend fun getCollections(page : Int, perPage : Int) : List<UnsplashCollection> {
        return try {
            unsplashService.unsplashApi.getCollections(
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
            logError(::getPhotos.name, e)
            listOf()
        }
    }

    suspend fun getPhotosInCollection(collectionId : String, page : Int, perPage : Int) : List<UnsplashPhoto> {
        return try {
            unsplashService.unsplashApi.getPhotosInCollection(
                collectionId,
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
            logError(::getPhotos.name, e)
            listOf()
        }
    }

    suspend fun getUserProfile() : UnsplashUserProfile? {
        return try {
            unsplashService.unsplashApi.getUserProfile()
        } catch (e: UnknownHostException) {
            handleUnknownHostError(e)
            null
        } catch (e: HttpException) {
            handleHttpException(e)
            null
        } catch (e: Exception) {
            logError(::getUserProfile.name, e)
            null
        }
    }

    suspend fun getLikedPhotos(username: String, page : Int, perPage : Int) : List<UnsplashPhoto> {
        return try {
            unsplashService.unsplashApi.getLikedPhotos(
                username,
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
            logError(::getPhotos.name, e)
            listOf()
        }
    }

}