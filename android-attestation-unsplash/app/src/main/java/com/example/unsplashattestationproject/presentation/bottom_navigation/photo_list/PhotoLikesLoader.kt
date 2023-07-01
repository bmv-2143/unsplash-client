package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class PhotoLikesLoader {

    companion object {

        fun updateLikes(binding: PhotoListItemBinding, likesData: Pair<Boolean, Int>) {
            updateLikeButtonState(binding, likesData.first)
            updatePhotoLikesCount(binding, likesData.second)
        }

        private fun updateLikeButtonState(binding: PhotoListItemBinding, isLiked: Boolean) {
            binding.photoListItemLikeButton.setImageResource(
                if (isLiked) {
                    R.drawable.photo_list_item_favorite_filled
                } else {
                    R.drawable.photo_list_item_favorite_empty
                }
            )
        }

        private fun updatePhotoLikesCount(binding: PhotoListItemBinding, likesCount: Int) {
            binding.photoListItemLikeCount.text = likesCount.toString()
        }
    }

}