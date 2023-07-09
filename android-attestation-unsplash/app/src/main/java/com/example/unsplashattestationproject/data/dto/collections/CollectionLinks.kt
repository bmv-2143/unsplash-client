package com.example.unsplashattestationproject.data.dto.collections

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionLinks(
    val self: String,
    val html: String,
    val photos: String,
    val related: String
)