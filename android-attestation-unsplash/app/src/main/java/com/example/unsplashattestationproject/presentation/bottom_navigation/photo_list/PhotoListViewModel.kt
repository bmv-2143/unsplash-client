package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.domain.GetPhotosUseCase
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PhotoListViewModel @Inject constructor(getPhotosUseCase: GetPhotosUseCase) :
    ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "PHOTO LIST"
    }
    val text: LiveData<String> = _text

    private val pagedPhotosFlow =
        getPhotosUseCase().cachedIn(viewModelScope)

    fun getPhotosPagedFlow(): Flow<PagingData<UnsplashPhoto>> {

        Log.e(TAG, "getPhotosPagedFlow...")

        return pagedPhotosFlow
    }
}