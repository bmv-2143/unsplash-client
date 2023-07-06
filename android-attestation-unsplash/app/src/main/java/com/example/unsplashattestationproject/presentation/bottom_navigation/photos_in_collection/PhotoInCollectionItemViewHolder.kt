package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoItemLoader
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel

class PhotoInCollectionItemViewHolder(
    private val binding: PhotoListItemBinding,
    val onClick: (PhotoListItemUiModel) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: PhotoListItemUiModel? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(photoItem: PhotoListItemUiModel) {
        currentItem = photoItem
        PhotoItemLoader(binding).loadData(photoItem)
    }

}