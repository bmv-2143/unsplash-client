package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.domain.GetPhotosInCollectionsUseCase
import com.example.unsplashattestationproject.presentation.bottom_navigation.model.toPhotoListItemUiModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PhotosInCollectionViewModel @Inject constructor(
    private val getPhotosInCollectionUseCase: GetPhotosInCollectionsUseCase) : ViewModel() {

    fun getPhotosInCollection(collectionId: String): Flow<PagingData<PhotoListItemUiModel>> {
        return getPhotosInCollectionUseCase(collectionId).map { pagingData: PagingData<Photo> ->
            pagingData.map { photo: Photo ->
                photo.toPhotoListItemUiModel()
            }
        }.cachedIn(viewModelScope)
    }

}