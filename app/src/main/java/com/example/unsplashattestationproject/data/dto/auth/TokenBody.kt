package com.example.unsplashattestationproject.data.dto.auth

import com.example.unsplashattestationproject.data.network.AuthQuery
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class TokenBody(
    @Json(name = "client_id") val clientId: String = AuthQuery.VAL_ACCESS_KEY,
    @Json(name = "client_secret") val clientSecret: String = AuthQuery.CLIENT_ID_SECRET_KEY,
    @Json(name = "redirect_uri") val redirectUri: String = AuthQuery.VAL_REDIRECT_URL,
    val code: String,
    @Json(name = "grant_type") val grantType: String = AuthQuery.VAL_AUTH_CODE,
)