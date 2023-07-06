package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.FragmentPhotosInCollectionBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.textutils.getFormattedTags

class PhotosInCollectionFragment : Fragment() {

    private var _binding: FragmentPhotosInCollectionBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosInCollectionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFragmentTitle()

        activityViewModel.selectedCollection?.let {
            updateCollectionTexts(it)
        }
    }

    private fun updateCollectionTexts(collection : UnsplashCollection) {
        binding.fragmentPhotosInCollectionTitle.text = collection.title
        updateCollectionTags(collection)
        updateCollectionDescription(collection)
        updateCollectionTotalPhotosAndAuthor(collection)
    }

    private fun updateCollectionTotalPhotosAndAuthor(collection: UnsplashCollection) {
        binding.fragmentPhotosInCollectionTotalImagesAndAuthor.text = getString(
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

    private fun setFragmentTitle() {
        val title = activityViewModel.selectedCollection?.title ?: ""
        (activity as AppCompatActivity).supportActionBar?.title = title
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
