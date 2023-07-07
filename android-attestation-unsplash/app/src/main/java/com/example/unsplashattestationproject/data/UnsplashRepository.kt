package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.unsplashattestationproject.FEATURE_FLAG_REMOTE_MEDIATOR
import com.example.unsplashattestationproject.data.downloads.UnsplashDownloader
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.dto.profile.UnsplashUserProfile
import com.example.unsplashattestationproject.data.pagingsource.GetCollectionsPagingSource
import com.example.unsplashattestationproject.data.pagingsource.GetPhotosInCollectionPagingSource
import com.example.unsplashattestationproject.data.pagingsource.SearchPhotosPagingSource
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.log.TAG
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


const val PAGE_SIZE = 30
const val PREFETCH_DISTANCE =  PAGE_SIZE / 3

@Singleton
class UnsplashRepository @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val unsplashDownloader: UnsplashDownloader,
    @Named("mediatorPager") private val mediatorPager: Pager<Int, Photo>,
    @Named("getPhotosSimplePager") private val simplePager: Pager<Int, Photo>,
) {

    init {
        unsplashAccessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
        Log.e(TAG, "ACCESS_TOKEN: $unsplashAccessToken")
    }

    val networkErrorsFlow = unsplashNetworkDataSource.networkErrorsFlow

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

    fun getPhotosFlow(): Flow<PagingData<Photo>> {
        return if (FEATURE_FLAG_REMOTE_MEDIATOR) {
            mediatorPager.flow
        } else {
            simplePager.flow
        }
    }

    suspend fun getPhotoDetails(photoId: String): UnsplashPhotoDetails? =
        unsplashNetworkDataSource.getPhotoDetails(photoId)

    fun getTrackedDownloadPhotoUrl(photo: UnsplashPhotoDetails) {
        unsplashDownloader.downloadFile(
            photo.urls.raw,
            getFileNameForDownload(photo.id + "_RAW"),
            unsplashAccessToken
        )
    }

    fun startTrackedDownload(url: String, id: String) {
        unsplashDownloader.downloadFile(
            url,
            getFileNameForDownload(id),
            unsplashAccessToken
        )
    }

    suspend fun getTrackedDownloadPhotoUrl(photoId: String): String? =
        unsplashNetworkDataSource.getTrackedDownloadPhotoUrl(photoId)

    private fun getFileNameForDownload(photoId: String): String {
        return "unsplash-photo-$photoId.jpeg"
    }


    suspend fun updateLikeStatus(photoId: String, isLiked: Boolean): UnsplashPhoto? {
        return if (isLiked) {
            likePhoto(photoId)
        } else {
            unlikePhoto(photoId)
        }
    }

    private suspend fun likePhoto(photoId: String): UnsplashPhoto? {
        return unsplashNetworkDataSource.likePhoto(photoId)?.photo
    }

    private suspend fun unlikePhoto(photoId: String): UnsplashPhoto? {
        return unsplashNetworkDataSource.unlikePhoto(photoId)?.photo
    }

    internal fun searchPhotos(query: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { SearchPhotosPagingSource(query, unsplashNetworkDataSource) }
        ).flow
    }

    internal fun getCollections(): Flow<PagingData<UnsplashCollection>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { GetCollectionsPagingSource(unsplashNetworkDataSource) }
        ).flow
    }

    internal fun getPhotosInCollection(collectionId: String): Flow<PagingData<Photo>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                initialLoadSize = PAGE_SIZE
            ),
            pagingSourceFactory = { GetPhotosInCollectionPagingSource(
                unsplashNetworkDataSource,
                collectionId,
            ) }
        ).flow
    }

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var unsplashAccessToken: String = ""
            private set
    }

    suspend fun getUserProfile(): UnsplashUserProfile? =
        unsplashNetworkDataSource.getUserProfile()
}