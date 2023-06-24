package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class PhotosAdapterViewHolder(
    private val binding: PhotoListItemBinding,
    val onClick: (UnsplashPhoto) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private var currentItem: UnsplashPhoto? = null

    init {
        binding.root.setOnClickListener {
            currentItem?.let {
                onClick(it)
            }
        }
    }

    fun bind(photoItem: UnsplashPhoto) {
        currentItem = photoItem
        binding.photoListItemText.text = photoItem.description
    }

}