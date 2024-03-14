package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashPhotoLinks(
    @Json(name = "self") val self: String,
    @Json(name = "html") val html: String,
    @Json(name = "download") val download: String,
    @Json(name = "download_location") val downloadLocation: String
)