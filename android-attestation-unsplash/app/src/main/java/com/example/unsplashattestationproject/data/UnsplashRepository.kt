package com.example.unsplashattestationproject.data

import android.content.SharedPreferences
import android.util.Log
import com.example.unsplashattestationproject.log.TAG
import javax.inject.Inject

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
        val editor = sharedPreferences.edit()
        editor.putString("access_token", accessToken)
        editor.apply()
    }

}