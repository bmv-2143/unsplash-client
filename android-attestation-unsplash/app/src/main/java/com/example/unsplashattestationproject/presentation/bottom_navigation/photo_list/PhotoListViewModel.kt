package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.unsplashattestationproject.data.room.entities.Photo
import com.example.unsplashattestationproject.domain.GetPhotosUseCase
import com.example.unsplashattestationproject.domain.SearchPhotosUseCase
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(
    getPhotosUseCase: GetPhotosUseCase,
    val searchPhotosUseCase: SearchPhotosUseCase
) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PHOTO LIST"
    }
    val dummyText: LiveData<String> = _text

    var isSearchMode = false
    var currentQuery = ""
        private set

    private val pagedPhotosFlow = cacheInPhotoPagingFlow(getPhotosUseCase())

    private fun cacheInPhotoPagingFlow(input : Flow<PagingData<Photo>>) : Flow<PagingData<PhotoListItemUiModel>> {
        return input.map { pagingData: PagingData<Photo> ->
            pagingData.map { photo: Photo ->
                photo.toPhotoListItemUiModel()
            }
        }.cachedIn(viewModelScope)
    }

    private fun Photo.toPhotoListItemUiModel(): PhotoListItemUiModel {
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

    fun getPhotosPagedFlow(): Flow<PagingData<PhotoListItemUiModel>> {
        Log.e(TAG, "getPhotosPagedFlow...")
        return pagedPhotosFlow
    }

    private val _uiStateFlow = MutableSharedFlow<PhotoListFragmentState>(
        replay = 100,
        onBufferOverflow = BufferOverflow.SUSPEND
    )
    val uiStatesFlow = _uiStateFlow.asSharedFlow()

    var dataIsLoading: Boolean = false
        private set

    fun handleAdapterUpdates(loadStates: CombinedLoadStates) {
        updateDataIsLoading(loadStates)
        viewModelScope.launch {
            when (loadStates.refresh) {
                is LoadState.Loading -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageLoading)
                }

                is LoadState.NotLoading -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageNotLoading)
                    handleNextPageLoading(loadStates)
                }

                is LoadState.Error -> {
                    _uiStateFlow.emit(PhotoListFragmentState.FirstPageLoadError)
                }
            }
        }
    }

    private suspend fun handleNextPageLoading(loadStates: CombinedLoadStates) {
        when (loadStates.append) {
            is LoadState.Loading -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageLoading)
            }

            is LoadState.NotLoading -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageNotLoading)
            }

            is LoadState.Error -> {
                _uiStateFlow.emit(PhotoListFragmentState.NextPageLoadError)
            }
        }
    }

    private fun updateDataIsLoading(loadStates: CombinedLoadStates) {
        dataIsLoading =
            (loadStates.refresh is LoadState.Loading) || (loadStates.append is LoadState.Loading)
    }

    private val _searchResults = MutableStateFlow<Flow<PagingData<PhotoListItemUiModel>>>(emptyFlow())
    val searchResults: StateFlow<Flow<PagingData<PhotoListItemUiModel>>> = _searchResults

    fun startSearch(query: String) {
        currentQuery = query
        _searchResults.value = cacheInPhotoPagingFlow(searchPhotosUseCase(query))
    }

    fun clearSearchResults() {
        currentQuery = ""
        _searchResults.value = emptyFlow()
    }

}