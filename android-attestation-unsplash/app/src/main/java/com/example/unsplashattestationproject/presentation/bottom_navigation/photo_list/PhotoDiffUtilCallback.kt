package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto

class PhotoDiffUtilCallback : DiffUtil.ItemCallback<UnsplashPhoto>() {

    override fun areItemsTheSame(
        oldItem: UnsplashPhoto,
        newItem: UnsplashPhoto
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: UnsplashPhoto,
        newItem: UnsplashPhoto
    ): Boolean {
        return oldItem == newItem
    }
}