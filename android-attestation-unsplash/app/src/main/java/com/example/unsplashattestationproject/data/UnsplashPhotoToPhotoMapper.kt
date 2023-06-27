package com.example.unsplashattestationproject.data

import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.room.entities.Photo

fun UnsplashPhoto.toPhoto(): Photo {
    return Photo(
        id = id,
        createdAt = createdAt,
        updatedAt = updatedAt,
        width = width,
        height = height,
        color = color,
        blurHash = blurHash,
        description = description,
        altDescription = altDescription,
        urlsRaw = urls.raw,
        urlsFull = urls.full,
        urlsRegular = urls.regular,
        urlsSmall = urls.small,
        urlsThumb = urls.thumb,
        linksSelf = links.self,
        linksHtml = links.html,
        linksDownload = links.download,
        linksDownloadLocation = links.downloadLocation,
        likes = likes,
        likedByUser = likedByUser,
        userId = user.id,
        userName = user.name,
        userNickname = user.username,
    )
}