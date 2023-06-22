package com.example.unsplashattestationproject.data

import android.util.Log
import javax.inject.Inject

class UnsplashRepository @Inject constructor(private val unsplashNetworkDataSource: UnsplashNetworkDataSource) {

    suspend fun getAccessToken(authCode: String) : String {
        kotlin.runCatching {
            val authInfo = unsplashNetworkDataSource.getAccessToken(authCode)
            authInfo
        }.fold(
            onSuccess = { authInfo ->
                Log.e("TOKEN", authInfo.accessToken)
                return authInfo.accessToken
            },
            onFailure = {
                Log.e("TOKEN", it.message ?: "")
                return ""
            }
        )
    }

}