package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unsplashattestationproject.domain.GetCollectionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectionsViewModel @Inject constructor(getCollectionsUseCase: GetCollectionsUseCase): ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "COLLECTIONS"
    }
    val text: LiveData<String> = _text

    internal val collectionsPagedFlow = getCollectionsUseCase().cachedIn(viewModelScope)

}