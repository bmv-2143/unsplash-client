package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashLikeResponse(
    @Json(name = "photo") val photo: UnsplashPhoto,
    @Json(name = "user") val user: UnsplashUser
)