package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class UnsplashTrackedDownloadResponse(
    @Json(name = "url")
    val url: String,
)
