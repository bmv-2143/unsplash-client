package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import android.util.Log
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UnsplashRepository @Inject constructor(
    private val unsplashNetworkDataSource: UnsplashNetworkDataSource,
    private val sharedPreferences: SharedPreferences
    ) {

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
        editor.putString("access_token", accessToken)
        editor.apply()
    }

    private fun cacheToken(accessToken: String) {
        unsplashAccessToken = accessToken
    }

    suspend fun getPhotos() : Result<List<UnsplashPhoto>> {
        return try {
            Result.success(unsplashNetworkDataSource.getPhotos())
        } catch (e: Exception) {
            Log.e(TAG, "${::getPhotos.name} error: ${e.message}")
            Result.failure(e)
        }
    }

    companion object {
        var unsplashAccessToken: String = ""
            private set
    }
}