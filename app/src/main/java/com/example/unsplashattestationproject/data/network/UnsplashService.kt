package com.example.unsplashattestationproject.data.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UnsplashService @Inject constructor(@Named("api") retrofitUnsplashService: Retrofit) {

    val unsplashApi: UnsplashApi =
        retrofitUnsplashService.create(UnsplashApi::class.java)

}