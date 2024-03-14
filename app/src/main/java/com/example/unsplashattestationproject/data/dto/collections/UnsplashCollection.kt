package com.example.unsplashattestationproject.data.dto.collections

import com.example.unsplashattestationproject.data.dto.photos.Tag
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashCollection(
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
    @Json(name = "share_key") val shareKey: String?,
    val tags: List<Tag>?,
    val links: CollectionLinks,
    val user: UnsplashCollectionUser,
    @Json(name = "cover_photo") val coverPhoto: CoverPhoto?
)