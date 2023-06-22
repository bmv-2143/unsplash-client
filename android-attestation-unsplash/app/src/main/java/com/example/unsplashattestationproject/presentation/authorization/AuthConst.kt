package com.example.unsplashattestationproject.presentation.authorization

class AuthConst {

    // sample auth url:
    // https://unsplash.com/oauth/authorize?client_id=<registered_app_key>&redirect_url=
    //com.example.appauthguide//auth&response_type=code&scope=public

    companion object {
        const val AUTH_URL = "https://unsplash.com/oauth/authorize"
        const val REDIRECT_URL = "com.example.unsplashattestationproject://auth"
        const val CLIENT_ID_ACCESS_KEY = "YOUR_UNSPLASH_ACCESS_KEY"
        const val CLIENT_ID_SECRET_KEY = "YOUR_UNSPLASH_SECRET_KEY"
    }

    // TODO: Например, для Unsplash необходимо зарегистрировать redirect_url в консоли разработчика,
    //  иначе flow будет выдавать ошибку



}