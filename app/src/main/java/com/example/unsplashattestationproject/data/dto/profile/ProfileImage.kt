package com.example.unsplashattestationproject.data.dto.profile

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileImage(
    val small: String,
    val medium: String,
    val large: String
)