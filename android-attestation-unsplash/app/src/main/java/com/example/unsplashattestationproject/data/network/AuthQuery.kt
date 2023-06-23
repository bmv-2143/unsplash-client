package com.example.unsplashattestationproject.data.network

class AuthQuery {

    /* sample auth url:
    * https://unsplash.com/oauth/authorize?client_id=<registered_app_key>&redirect_url=com.example.appauthguide//auth&response_type=code&scope=public
    */

    companion object {
        const val AUTH_URL = "https://unsplash.com/oauth/authorize"
        const val CLIENT_ID_SECRET_KEY = "YOUR_UNSPLASH_SECRET_KEY"

        const val PARAM_CLIENT_ID: String = "client_id"
        const val VAL_ACCESS_KEY = "YOUR_UNSPLASH_ACCESS_KEY"

        const val PARAM_REDIRECT_URI: String = "redirect_uri"
        const val VAL_REDIRECT_URL = "com.example.unsplashattestationproject://auth"

        const val PARAM_RESPONSE_TYPE: String = "response_type"
        const val VAL_RESPONSE_TYPE_CODE: String = "code"

        const val PARAM_SCOPE: String = "scope"
        const val VAL_PUBLIC: String = "public"

        const val PARAM_CODE: String = "code"
    }
}