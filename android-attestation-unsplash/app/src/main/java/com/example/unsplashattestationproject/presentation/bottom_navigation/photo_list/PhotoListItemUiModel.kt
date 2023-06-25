package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

data class PhotoListItemUiModel(
    val id: String,
    val authorName: String,
    val authorUsername: String,
    val likes: Int,
    val likedByUser: Boolean,
    val imageUrl: String,
)