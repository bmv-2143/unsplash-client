package com.example.unsplashattestationproject.presentation.bottom_navigation.model

import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoDetails
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel

fun UnsplashPhotoDetails.toPhotoListItemUiModel() : PhotoListItemUiModel =
    PhotoListItemUiModel(
        id = -1,
        remoteId = this.id,
        authorName = this.user.name,
        authorUsername = this.user.username,
        authorAvatar = this.user.profileImage.medium,
        likes = this.likes,
        likedByUser = this.likedByUser,
        imageUrl = this.urls.regular,
    )

fun Photo.toPhotoListItemUiModel(): PhotoListItemUiModel {
    return PhotoListItemUiModel(
        id = id,
        remoteId = remoteId,
        authorName = userName,
        authorUsername = userNickname,
        authorAvatar = userAvatar,
        likes = likes,
        likedByUser = likedByUser,
        imageUrl = urlsRegular
    )
}