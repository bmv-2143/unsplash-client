package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class PhotosAdapterViewHolder(
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