package com.example.unsplashattestationproject.presentation.bottom_navigation.liked_photos_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.domain.GetLikedPhotosUseCase
import com.example.unsplashattestationproject.presentation.bottom_navigation.model.toPhotoListItemUiModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LikedPhotosViewModel @Inject constructor(private val getLikedPhotosUseCase: GetLikedPhotosUseCase) :
    ViewModel() {

    // ???? nested flows...
    private val _likedPhotos = MutableStateFlow<Flow<PagingData<PhotoListItemUiModel>>>(emptyFlow())
    val likedPhotos: StateFlow<Flow<PagingData<PhotoListItemUiModel>>> = _likedPhotos

    internal fun loadLikedPhotos(username: String) {
        _likedPhotos.value = cacheInPhotoPagingFlow(getLikedPhotosUseCase(username))
    }

    private fun cacheInPhotoPagingFlow(input: Flow<PagingData<Photo>>): Flow<PagingData<PhotoListItemUiModel>> {
        return input.map { pagingData: PagingData<Photo> ->
            pagingData.map { photo: Photo ->
                photo.toPhotoListItemUiModel()
            }
        }.cachedIn(viewModelScope)
    }

}