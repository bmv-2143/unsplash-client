package com.example.unsplashattestationproject.data.network

import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.data.dto.photos.UnsplashLikeResponse
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.dto.photos.UnsplashSearchResult
import com.example.unsplashattestationproject.data.dto.photos.UnsplashTrackedDownloadResponse
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @GET("photos/{id}/download")
    suspend fun getTrackedDownloadPhoto(@Path("id") id: String): UnsplashTrackedDownloadResponse

    @POST("photos/{id}/like")
    suspend fun likePhoto(@Path("id") id: String) : UnsplashLikeResponse

    @DELETE("photos/{id}/like")
    suspend fun unlikePhoto(@Path("id") id: String) : UnsplashLikeResponse

    @GET("search/photos")
    suspend fun searchPhotos(
        @Query("query") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): UnsplashSearchResult

    @GET("collections")
    suspend fun getCollections(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
    ): List<UnsplashCollection>
}