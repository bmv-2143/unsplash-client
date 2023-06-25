package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class PhotosPagedAdapter(
    private val onItemClicked: (PhotoListItemUiModel) -> Unit
) : PagingDataAdapter<PhotoListItemUiModel, PhotosAdapterViewHolder>(PhotoDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        val binding = PhotoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: PhotosAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}