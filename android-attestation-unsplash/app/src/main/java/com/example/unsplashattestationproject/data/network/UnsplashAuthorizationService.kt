package com.example.unsplashattestationproject.data.network

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named

class UnsplashAuthorizationService @Inject constructor(
    @Named("auth") retrofitAuthService: Retrofit) {

    val unsplashAuthApi: UnsplashAuthorizationApi =
        retrofitAuthService.create(UnsplashAuthorizationApi::class.java)

}