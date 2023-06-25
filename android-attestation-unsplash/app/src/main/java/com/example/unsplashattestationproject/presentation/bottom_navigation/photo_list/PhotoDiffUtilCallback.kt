package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.recyclerview.widget.DiffUtil

class PhotoDiffUtilCallback : DiffUtil.ItemCallback<PhotoListItemUiModel>() {

    override fun areItemsTheSame(
        oldItem: PhotoListItemUiModel,
        newItem: PhotoListItemUiModel
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PhotoListItemUiModel,
        newItem: PhotoListItemUiModel
    ): Boolean {
        return oldItem == newItem
    }
}