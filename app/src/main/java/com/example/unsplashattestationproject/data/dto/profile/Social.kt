package com.example.unsplashattestationproject.data.dto.profile

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Social(
    @Json(name = "instagram_username") val instagramUsername: String?,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "paypal_email") val paypalEmail: String?
)