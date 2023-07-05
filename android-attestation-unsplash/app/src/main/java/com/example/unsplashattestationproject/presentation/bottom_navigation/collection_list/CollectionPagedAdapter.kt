package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.CollectionListItemBinding

class CollectionPagedAdapter(
    private val onItemClicked: (UnsplashCollection) -> Unit
) : PagingDataAdapter<UnsplashCollection, CollectionAdapterViewHolder>(CollectionDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionAdapterViewHolder {
        val binding = CollectionListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: CollectionAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }
}