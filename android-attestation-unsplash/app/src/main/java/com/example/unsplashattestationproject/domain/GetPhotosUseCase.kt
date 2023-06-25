package com.example.unsplashattestationproject.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.example.unsplashattestationproject.data.PhotoRemoteMediator
import com.example.unsplashattestationproject.data.UnsplashRepository
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoLinks
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhotoUrls
import com.example.unsplashattestationproject.data.room.entities.Photo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
//    private val photosPagingSource: PhotosPagingSource,
    private val unsplashRepository: UnsplashRepository,
    private val photoRemoteMediator: PhotoRemoteMediator
) {

    @OptIn(ExperimentalPagingApi::class)
    operator fun invoke(): Flow<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = photoRemoteMediator,
            pagingSourceFactory = { unsplashRepository.getPhotosFromDb() }
        ).flow.map { pagingData: PagingData<Photo> ->
            pagingData.map { photo: Photo ->
                photo.toUnsplashPhoto()
            }
        }

    }

    private fun Photo.toUnsplashPhoto(): UnsplashPhoto {
        return UnsplashPhoto(
            id = id,
            createdAt = createdAt,
//            updatedAt = updatedAt,
//            width = width,
//            height = height,
//            color = color,
//            blurHash = blurHash,
//            description = description,
//            altDescription = altDescription,
            urls = UnsplashPhotoUrls(
                raw = urlsRaw,
                full = urlsFull,
                regular = urlsRegular,
                small = urlsSmall,
                thumb = urlsThumb
            ),
            links = UnsplashPhotoLinks(
                self = linksSelf,
                html = linksHtml,
                download = linksDownload,
                downloadLocation = linksDownloadLocation
            ),
            likes = likes,
            likedByUser = likedByUser,
            currentUserCollections = emptyList(),
            sponsorship = null,
            user = null
//            user = UnsplashUser(
//                id = userId,
//                updatedAt = "",
//                username = "",
//                name = "",
//                firstName = "",
//                lastName = null,
//                twitterUsername = null,
//                portfolioUrl = null,
//                bio = null,
//                location = null,
//                links = UnsplashUserLinks("", "", "", "", ""),
//                profileImage =
//                UnsplashUserProfileImage("", "", ""),
//                instagramUsername =
//                null, totalCollections =
//                0, totalLikes =
//                0, totalPhotos =
//                0, acceptedTos =
//                false
            )
    }
}