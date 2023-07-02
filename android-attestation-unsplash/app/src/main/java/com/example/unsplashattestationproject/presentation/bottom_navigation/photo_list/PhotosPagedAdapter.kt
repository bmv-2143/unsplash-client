package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding

class PhotosPagedAdapter(
    private val onItemClicked: (PhotoListItemUiModel) -> Unit
) : PagingDataAdapter<PhotoListItemUiModel, PhotosAdapterViewHolder>(PhotoDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosAdapterViewHolder {
        val binding = PhotoListItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return PhotosAdapterViewHolder(binding, onItemClicked)
    }

    override fun onBindViewHolder(holder: PhotosAdapterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    fun updateItemLikes(updatedItem: PhotoListItemUiModel) {
        val currentList = snapshot()
        val index = currentList.indexOfFirst { it?.remoteId == updatedItem.remoteId }

        if (index == -1)
            return

        getItem(index)?.let {
            it.likedByUser = updatedItem.likedByUser
            it.likes = updatedItem.likes
            Log.e(TAG, "updateItem: notifyItemChanged: $index")
            notifyItemChanged(index)
        }
    }
}