package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import android.util.Log
import androidx.paging.PagingSource
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.room.PhotoDatabase
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val sharedPreferences: SharedPreferences,
    private val photoDatabase: PhotoDatabase
    ) {

    init {
        unsplashAccessToken = sharedPreferences.getString(PREFS_KEY_ACCESS_TOKEN, "") ?: ""
    }

    suspend fun getAccessToken(authCode: String) : String {
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

    private fun saveAccessToken(accessToken: String) {
        cacheToken(accessToken)

        val editor = sharedPreferences.edit()
        editor.putString(PREFS_KEY_ACCESS_TOKEN, accessToken)
        editor.apply()
    }

    private fun cacheToken(accessToken: String) {
        unsplashAccessToken = accessToken
    }

    suspend fun getPhotos(page : Int) : List<UnsplashPhoto> {
        return unsplashNetworkDataSource.getPhotos(page)
    }

    // ----------

    fun getPhotosFromDb(): PagingSource<Int, Photo> {
        return photoDatabase.photoDao().getPhotos()
    }

    suspend fun savePhotosToDb(photos: List<UnsplashPhoto>) {
        photoDatabase.photoDao().insertPhotos(photos.map { it.toPhoto() })
    }

    suspend fun clearPhotosFromDb() {
        photoDatabase.photoDao().clearPhotos()
    }

    private fun UnsplashPhoto.toPhoto(): Photo {
        return Photo(
            id = id,
            createdAt = createdAt,
            updatedAt = updatedAt,
            width = width,
            height = height,
            color = color,
            blurHash = blurHash,
            description = description,
            altDescription = altDescription,
            urlsRaw = urls.raw,
            urlsFull = urls.full,
            urlsRegular = urls.regular,
            urlsSmall = urls.small,
            urlsThumb = urls.thumb,
            linksSelf = links.self,
            linksHtml = links.html,
            linksDownload = links.download,
            linksDownloadLocation = links.downloadLocation,
            likes = likes,
            likedByUser = likedByUser,
            userId = user.id,
            userName = user.name,
            userNickname = user.username,
        )
    }

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var unsplashAccessToken: String = ""
            private set
    }
}