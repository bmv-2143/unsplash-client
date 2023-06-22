package com.example.unsplashattestationproject.data.network

import retrofit2.Retrofit
import javax.inject.Inject

class UnsplashService @Inject constructor(retrofitService: Retrofit) {

    val unsplashApi: UnsplashApi = retrofitService.create(UnsplashApi::class.java)

}