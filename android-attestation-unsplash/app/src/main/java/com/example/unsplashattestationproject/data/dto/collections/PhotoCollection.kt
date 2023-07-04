package com.example.unsplashattestationproject.data.dto.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class PhotoCollection(val dummyData: String = "") {
}

@JsonClass(generateAdapter = true)
data class Collection(
    val id: String,
    val title: String,
    val description: String?,
    @Json(name = "published_at") val publishedAt: String,
    @Json(name = "last_collected_at") val lastCollectedAt: String?,
    @Json(name = "updated_at") val updatedAt: String,
    val curated: Boolean,
    @Json(name = "featured") val isFeatured: Boolean,
    @Json(name = "total_photos") val totalPhotos: Int,
    val private: Boolean,
    val share_key: String?,
    val tags: List<Tag>?,
    val links: CollectionLinks,
    val user: User,
    val cover_photo: CoverPhoto?
)

@JsonClass(generateAdapter = true)
data class Tag(
    val title: String
)

@JsonClass(generateAdapter = true)
data class CollectionLinks(
    val self: String,
    val html: String,
    val photos: String,
    val related: String
)

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

@JsonClass(generateAdapter = true)
data class ProfileImageUrls(
    @Json(name="small")val small:String,
    @Json(name="medium")val medium:String,
    @Json(name="large")val large:String
)

@JsonClass(generateAdapter = true)
data class CoverPhoto(
    val id:String,
    @Json(name="created_at")val createdAt:String,
    @Json(name="updated_at")val updatedAt:String?,
    @Json(name="width")val width:Int,
    @Json(name="height")val height:Int,
    @Json(name="color")val color:String?,
    @Json(name="blur_hash")val blurHash:String?,
    @Json(name="description")val description:String?,
    @Json(name="alt_description")val altDescription:String?,
    @Json(name="urls")val urls : Urls
)

@JsonClass(generateAdapter=true)
data class Urls(
    @Json(name="raw")val raw:String,
    @Json(name="full")val full:String,
    @Json(name="regular")val regular:String,
    @Json(name="small")val small:String,
    @Json(name="thumb")val thumb:String
)