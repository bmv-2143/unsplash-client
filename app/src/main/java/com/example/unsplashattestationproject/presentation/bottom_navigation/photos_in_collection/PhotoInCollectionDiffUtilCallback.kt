package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel

class PhotoInCollectionDiffUtilCallback : DiffUtil.ItemCallback<PhotoListItemUiModel>() {

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