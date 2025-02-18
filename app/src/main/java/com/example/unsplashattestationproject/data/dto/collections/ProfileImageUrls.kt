package com.example.unsplashattestationproject.data.dto.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProfileImageUrls(
    @Json(name="small")val small:String,
    @Json(name="medium")val medium:String,
    @Json(name="large")val large:String
)