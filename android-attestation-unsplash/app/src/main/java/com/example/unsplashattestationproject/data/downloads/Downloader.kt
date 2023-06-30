package com.example.unsplashattestationproject.data.downloads


fun interface Downloader {

    fun downloadFile(url: String, accessToken : String): Long

}