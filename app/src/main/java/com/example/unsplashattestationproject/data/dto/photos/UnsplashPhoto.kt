package com.example.unsplashattestationproject.data.dto.photos

import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UnsplashPhoto(
    @Json(name = "id") val id: String,
    @Json(name = "created_at") val createdAt: String,
    @Json(name = "updated_at") val updatedAt: String,
    @Json(name = "width") val width: Int,
    @Json(name = "height") val height: Int,
    @Json(name = "color") val color: String,
    @Json(name = "blur_hash") val blurHash: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "alt_description") val altDescription: String?,
    @Json(name = "urls") val urls: UnsplashUrls,
    @Json(name = "links") val links: UnsplashPhotoLinks,
    @Json(name = "likes") val likes: Int,
    @Json(name = "liked_by_user") val likedByUser: Boolean,
    @Json(name = "current_user_collections") val currentUserCollections: List<UnsplashCollection>,
    @Json(name = "sponsorship") val sponsorship: UnsplashSponsorship?,
    @Json(name = "user") val user: UnsplashUser
)