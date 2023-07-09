package com.example.unsplashattestationproject.data.dto.collections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

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