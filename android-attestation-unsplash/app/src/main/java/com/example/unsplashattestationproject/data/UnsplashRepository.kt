package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashattestationproject.FEATURE_FLAG_REMOTE_MEDIATOR
import com.example.unsplashattestationproject.data.downloads.UnsplashDownloader
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.log.TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


const val PAGE_SIZE = 10

@Singleton
class UnsplashRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val photoDatabase: PhotoDatabase,
    private val photoRemoteMediator: PhotoRemoteMediator,
    private val unsplashDownloader: UnsplashDownloader
) {

    init {
        unsplashAccessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    private fun saveAccessToken(accessToken: String) {
        Log.e(TAG, "ACCESS_TOKEN: $accessToken")
        cacheToken(accessToken)

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    suspend fun getAccessToken(authCode: String): String {
        kotlin.runCatching {
            val authInfo = unsplashNetworkDataSource.getAccessToken(authCode)
            authInfo
        }.fold(
            onSuccess = { authInfo ->
                Log.e(TAG, "Authorization success, access token: ${authInfo.accessToken}")
                saveAccessToken(authInfo.accessToken)
                return authInfo.accessToken
            },
            onFailure = { throwable ->
                Log.e(TAG, "Authorization failed, error: ${throwable.message}")
                return ""
            }
        )
    }

    private fun cacheToken(accessToken: String) {
        unsplashAccessToken = accessToken
    }


    @OptIn(ExperimentalPagingApi::class)
    fun getPhotosFlow(): Flow<PagingData<Photo>> {
        return if (FEATURE_FLAG_REMOTE_MEDIATOR) {
            // todo: inject pager with DI
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE / 2,
                    initialLoadSize = PAGE_SIZE
                ),
                remoteMediator = photoRemoteMediator,
                pagingSourceFactory = { photoDatabase.photoDao().getPhotos() }
            ).flow
        } else {
            Pager(
                config = PagingConfig(
                    pageSize = PAGE_SIZE,
                    prefetchDistance = PAGE_SIZE / 2,
                    initialLoadSize = PAGE_SIZE
                ),
                pagingSourceFactory = { PhotosPagingSource(unsplashRepository = this) }
            ).flow
        }
    }

    suspend fun getPhotos(page: Int): List<UnsplashPhoto> {
        return unsplashNetworkDataSource.getPhotos(page, PAGE_SIZE)
    }

    suspend fun getPhotoDetails(photoId: String): UnsplashPhotoDetails =
        unsplashNetworkDataSource.getPhotoDetails(photoId)

    fun getTrackedDownloadPhotoUrl(photo : UnsplashPhotoDetails) {
        unsplashDownloader.downloadFile(
            photo.urls.raw,
            getFileNameForDownload(photo.id + "_RAW"),
            unsplashAccessToken
        )
    }

    fun startTrackedDownload(url : String, id : String) {
        unsplashDownloader.downloadFile(
            url,
            getFileNameForDownload(id),
            unsplashAccessToken
        )
    }

    suspend fun getTrackedDownloadPhotoUrl(photoId : String) : String =
        unsplashNetworkDataSource.getTrackedDownloadPhotoUrl(photoId)

    private fun getFileNameForDownload(photoId: String): String {
        return "unsplash-photo-$photoId.jpeg"
    }

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var unsplashAccessToken: String = ""
            private set
    }
}