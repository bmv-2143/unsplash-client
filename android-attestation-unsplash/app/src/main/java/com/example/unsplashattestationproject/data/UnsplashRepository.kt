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

    companion object {

        const val PREFS_KEY_ACCESS_TOKEN = "access_token"

        var unsplashAccessToken: String = ""
            private set
    }
}