package com.example.unsplashattestationproject.data.dto.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    val id: String,
    val username: String,
    val name: String,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    @Json(name = "bio") val biography: String?,
    @Json(name = "location") val location: String?,
    @Json(name = "total_likes") val totalLikes: Int,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "total_collections") val totalCollections: Int,
    @Json(name = "instagram_username") val instagramUsername: String?,
    @Json(name = "twitter_username")val twitterUsername:String?,
    @Json(name = "profile_image")val profileImageUrls : ProfileImageUrls,
)