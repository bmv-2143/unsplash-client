package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.BuildConfig

class AuthQuery {

    /* sample auth url:
    * https://unsplash.com/oauth/authorize?client_id=<registered_app_key>&redirect_url=com.example.appauthguide//auth&response_type=code&scope=public
    */

    companion object {
        const val AUTH_URL = "https://unsplash.com/oauth/authorize"
        const val CLIENT_ID_SECRET_KEY = BuildConfig.SECRET_KEY

        const val PARAM_CLIENT_ID: String = "client_id"
        const val VAL_ACCESS_KEY = BuildConfig.ACCESS_KEY

        const val PARAM_REDIRECT_URI: String = "redirect_uri"
        const val VAL_REDIRECT_URL = "com.example.unsplashattestationproject://auth"

        const val PARAM_RESPONSE_TYPE: String = "response_type"
        const val VAL_RESPONSE_TYPE_CODE: String = "code"

        const val PARAM_SCOPE: String = "scope"
        const val VAL_SCOPE_PUBLIC_WRITE_PHOTOS: String = "public write_likes"

        const val PARAM_CODE: String = "code"

        const val VAL_AUTH_CODE =  "authorization_code"
    }
}