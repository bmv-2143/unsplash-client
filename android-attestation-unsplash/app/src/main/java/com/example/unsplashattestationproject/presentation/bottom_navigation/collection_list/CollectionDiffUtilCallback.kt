package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashattestationproject.data.dto.collections.PhotoCollection
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto

class CollectionDiffUtilCallback : DiffUtil.ItemCallback<PhotoCollection>() {

    override fun areItemsTheSame(
        oldItem: PhotoCollection,
        newItem: PhotoCollection
    ): Boolean {
        return oldItem.dummyData == newItem.dummyData // TODO: change to real data
    }

    override fun areContentsTheSame(
        oldItem: PhotoCollection,
        newItem: PhotoCollection
    ): Boolean {
        return oldItem == newItem
    }
}