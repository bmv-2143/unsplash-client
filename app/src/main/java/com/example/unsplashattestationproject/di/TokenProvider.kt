package com.example.unsplashattestationproject.di

fun interface TokenProvider {
    fun getToken(): String
}