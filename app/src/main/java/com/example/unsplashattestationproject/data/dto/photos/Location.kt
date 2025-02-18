package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Location(
    @Json(name = "city") val city: String?,
    @Json(name = "country") val country: String?,
    @Json(name = "position") val position: Position?
)