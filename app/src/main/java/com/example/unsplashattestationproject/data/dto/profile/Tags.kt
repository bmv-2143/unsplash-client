package com.example.unsplashattestationproject.data.dto.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tags(
    @Json(name = "custom") val custom: List<Any>,
    @Json(name = "aggregated") val aggregated: List<Any>
)