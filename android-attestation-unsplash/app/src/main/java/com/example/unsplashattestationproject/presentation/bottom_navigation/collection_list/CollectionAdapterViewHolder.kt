package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.data.dto.collections.PhotoCollection
import com.example.unsplashattestationproject.databinding.PhotoCollectionListItemBinding
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class CollectionAdapterViewHolder(
    private val binding: PhotoCollectionListItemBinding,
    val onClick: (PhotoCollection) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: PhotoCollection? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(collectionItem: PhotoCollection) {
        currentItem = collectionItem
        binding.photoCollectionListItemText.text = collectionItem.dummyData // TODO: change to real data
    }

}