package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import android.content.ContentValues.TAG
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.PhotoListItemBinding
import com.example.unsplashattestationproject.databinding.PhotosInCollectionItemHeaderBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel

class PhotoInCollectionPagedAdapter(
    private val unsplashCollection: UnsplashCollection?,
    private val onItemClicked: (PhotoListItemUiModel) -> Unit,
) : PagingDataAdapter<PhotoListItemUiModel, RecyclerView.ViewHolder>(PhotoInCollectionDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                VIEW_TYPE_HEADER -> {
                    val binding = PhotosInCollectionItemHeaderBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    PhotosInCollectionHeaderViewHolder(binding)
                }

                VIEW_TYPE_ITEM -> {
                    val binding = PhotoListItemBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                    PhotoInCollectionItemViewHolder(binding, onItemClicked)
                }

                else -> throw IllegalArgumentException("Invalid view type")
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PhotosInCollectionHeaderViewHolder -> {
                holder.bind(unsplashCollection)
            }
            is PhotoInCollectionItemViewHolder -> {
                getItem(position - 1)?.let { holder.bind(it) }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) {
            VIEW_TYPE_HEADER
        } else {
            VIEW_TYPE_ITEM
        }
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

    companion object {
        private const val VIEW_TYPE_HEADER = 0
        private const val VIEW_TYPE_ITEM = 1
    }
}