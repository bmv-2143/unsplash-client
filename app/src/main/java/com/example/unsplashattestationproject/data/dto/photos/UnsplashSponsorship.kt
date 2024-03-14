package com.example.unsplashattestationproject.data.dto.photos

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashSponsorship(
    @Json(name = "impression_urls") val impressionUrls: List<String>,
    @Json(name = "tagline") val tagline: String,
    @Json(name = "tagline_url") val taglineUrl: String?,
    @Json(name = "sponsor") val sponsor: UnsplashUser
)