package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
//        @Query("order_by") orderBy: String // todo: do I need this?
    ): List<UnsplashPhoto>

    @GET("photos/{id}")
    suspend fun getPhotoDetails(@Path("id") id: String): UnsplashPhotoDetails

}