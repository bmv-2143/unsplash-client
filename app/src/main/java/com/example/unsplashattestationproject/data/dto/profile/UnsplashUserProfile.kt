package com.example.unsplashattestationproject.data.dto.profile

import com.example.unsplashattestationproject.data.dto.photos.UnsplashUserLinks
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashUserProfile(
    val id: String,
    @Json(name = "updated_at") val updatedAt: String,
    val username: String,
    val name: String,
    @Json(name = "first_name") val firstName: String,
    @Json(name = "last_name") val lastName: String?,
    @Json(name = "twitter_username") val twitterUsername: String?,
    @Json(name = "portfolio_url") val portfolioUrl: String?,
    val bio: String?,
    val location: String?,
    val links: UnsplashUserLinks,
    @Json(name = "profile_image") val profileImage: ProfileImage,
    @Json(name = "instagram_username") val instagramUsername: String?,
    @Json(name = "total_collections") val totalCollections: Int,
    @Json(name = "total_likes") val totalLikes: Int,
    @Json(name = "total_photos") val totalPhotos: Int,
    @Json(name = "accepted_tos") val acceptedTos: Boolean,
    @Json(name = "for_hire") val forHire: Boolean,
    val social: Social,
    @Json(name = "followed_by_user") val followedByUser: Boolean,
    val photos: List<Any>,
    val badge: Any?,
    val tags: Tags,
    @Json(name = "followers_count") val followersCount: Int,
    @Json(name = "following_count") val followingCount: Int,
    @Json(name = "allow_messages") val allowMessages: Boolean,
    @Json(name = "numeric_id") val numericId: Int,
    val downloads: Int,
    val meta: Meta,
    val uid: String,
    val confirmed: Boolean

)