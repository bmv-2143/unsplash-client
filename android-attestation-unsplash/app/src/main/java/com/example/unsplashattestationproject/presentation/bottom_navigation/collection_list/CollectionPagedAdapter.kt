package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplashattestationproject.data.dto.collections.PhotoCollection
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.databinding.PhotoCollectionListItemBinding
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class CollectionPagedAdapter(
    private val onItemClicked: (PhotoCollection) -> Unit
) : PagingDataAdapter<PhotoCollection, CollectionAdapterViewHolder>(CollectionDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionAdapterViewHolder {
        val binding = PhotoCollectionListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CollectionAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}