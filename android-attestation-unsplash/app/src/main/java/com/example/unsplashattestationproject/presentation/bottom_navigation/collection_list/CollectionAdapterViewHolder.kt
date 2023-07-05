package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.PhotoCollectionListItemBinding

class CollectionAdapterViewHolder(
    private val binding: PhotoCollectionListItemBinding,
    val onClick: (UnsplashCollection) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: UnsplashCollection? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(collectionItem: UnsplashCollection) {
        currentItem = collectionItem
        binding.photoCollectionListItemText.text = collectionItem.id // TODO: add other fields
    }

}