package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashCollection(
    @Json(name = "id") val id: Int,
    @Json(name = "title") val title: String
)