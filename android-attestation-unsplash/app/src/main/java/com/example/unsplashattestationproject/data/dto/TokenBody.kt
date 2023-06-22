package com.example.unsplashattestationproject.data.dto

import com.example.unsplashattestationproject.presentation.authorization.AuthConst


data class TokenBody(

    val client_id: String = AuthConst.CLIENT_ID_ACCESS_KEY,
    val client_secret: String = AuthConst.CLIENT_ID_SECRET_KEY,
    val redirect_uri: String = AuthConst.REDIRECT_URL,
    val code: String,
    val grant_type: String = "authorization_code",
)