package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection

class CollectionDiffUtilCallback : DiffUtil.ItemCallback<UnsplashCollection>() {

    override fun areItemsTheSame(
        oldItem: UnsplashCollection,
        newItem: UnsplashCollection
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UnsplashCollection,
        newItem: UnsplashCollection
    ): Boolean {
        return oldItem == newItem
    }
}