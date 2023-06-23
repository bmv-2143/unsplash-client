package com.example.unsplashattestationproject.data.dto.auth

import com.example.unsplashattestationproject.data.network.AuthQuery


data class TokenBody(

    val client_id: String = AuthQuery.VAL_ACCESS_KEY,
    val client_secret: String = AuthQuery.CLIENT_ID_SECRET_KEY,
    val redirect_uri: String = AuthQuery.VAL_REDIRECT_URL,
    val code: String,
    val grant_type: String = AuthQuery.VAL_AUTH_CODE,
)