package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import retrofit2.http.GET
import retrofit2.http.Query

interface UnsplashApi {

    @GET("photos")
    suspend fun getPhotos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
//        @Query("order_by") orderBy: String // todo: do I need this?
    ): List<UnsplashPhoto>

}