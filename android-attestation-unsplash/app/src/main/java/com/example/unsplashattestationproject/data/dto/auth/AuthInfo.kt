package com.example.unsplashattestationproject.data.dto.auth

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class AuthInfo(

    @Json(name = "access_token") val accessToken: String

)