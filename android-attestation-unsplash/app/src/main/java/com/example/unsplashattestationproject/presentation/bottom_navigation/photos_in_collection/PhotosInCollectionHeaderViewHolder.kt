package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.PhotosInCollectionItemHeaderBinding
import com.example.unsplashattestationproject.presentation.textutils.getFormattedTags

class PhotosInCollectionHeaderViewHolder(
    private val binding: PhotosInCollectionItemHeaderBinding,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(unsplashCollection: UnsplashCollection?) {
        updateCollectionTexts(unsplashCollection!!)
    }

    private fun updateCollectionTexts(collection : UnsplashCollection) {
        binding.fragmentPhotosInCollectionTitle.text = collection.title
        updateCollectionTags(collection)
        updateCollectionDescription(collection)
        updateCollectionTotalPhotosAndAuthor(collection)
    }

    private fun updateCollectionTotalPhotosAndAuthor(collection: UnsplashCollection) {
        binding.fragmentPhotosInCollectionTotalImagesAndAuthor.text = binding.root.context.getString(
            R.string.fragment_photos_in_collection_total_images_and_author,
            collection.totalPhotos,
            collection.user.username
        )
    }

    private fun updateCollectionDescription(collection: UnsplashCollection) {
        if (collection.description.isNullOrEmpty()) {
            binding.fragmentPhotosInCollectionDescription.visibility = View.GONE
        } else {
            binding.fragmentPhotosInCollectionDescription.text = collection.description
        }
    }

    private fun updateCollectionTags(collection: UnsplashCollection) {
        if (collection.tags.isNullOrEmpty()) {
            binding.fragmentPhotosInCollectionTags.visibility = View.GONE
        } else {
            binding.fragmentPhotosInCollectionTags.text = getFormattedTags(collection.tags)
        }
    }

}