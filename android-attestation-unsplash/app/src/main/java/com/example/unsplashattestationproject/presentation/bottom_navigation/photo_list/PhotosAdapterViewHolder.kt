package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.unsplashattestationproject.R
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
        setPhotoTexts(photoItem)
        loadCharacterImage(photoItem.urls.small)
    }

    private fun setPhotoTexts(photoItem: UnsplashPhoto) {
        binding.photoListItemAuthorName.text = photoItem.user.name
        binding.photoListItemAuthorNickname.text = photoItem.user.username
        binding.photoListItemLikeCount.text = photoItem.likes.toString()
    }

    private fun loadCharacterImage(imageUrl: String) {
        Glide.with(binding.root.context)
            .load(imageUrl)
            .placeholder(R.drawable.photo_list_item_image_placeholder)
            .into(binding.photoListItemImage)
    }

}