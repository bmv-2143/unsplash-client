package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.unsplashattestationproject.databinding.FragmentPhotoDetailsBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoItemLoader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    private val photoDetailsFragmentViewModel : PhotoDetailsFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoDetailsBinding.inflate(inflater, container, false)
        loadPhotoItemData()
        return binding.root
    }

    private fun loadPhotoItemData() {
        if (activityViewModel.selectedPhoto != null) {
            PhotoItemLoader(binding.photoItem).loadData(activityViewModel.selectedPhoto!!)
        } else {
            Log.e(TAG, "onCreateView: selectedPhoto is null")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPhotoDetails()
        photoDetailsFragmentViewModel.loadPhotoDetails(activityViewModel.selectedPhoto!!.remoteId)
    }

    private fun observerPhotoDetails() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoDetailsFragmentViewModel.photoDetailsFlow.collect { photoDetails ->
                    Log.e(TAG, "PHOTO DETAILS: $photoDetails")
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}