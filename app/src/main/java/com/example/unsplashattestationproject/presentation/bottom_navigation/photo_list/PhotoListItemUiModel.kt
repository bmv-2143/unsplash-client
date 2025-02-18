package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

data class PhotoListItemUiModel(
    val id: Long,
    val remoteId: String,
    val authorName: String,
    val authorUsername: String,
    val authorAvatar: String,
    var likes: Int,
    var likedByUser: Boolean,
    val imageUrl: String,
)