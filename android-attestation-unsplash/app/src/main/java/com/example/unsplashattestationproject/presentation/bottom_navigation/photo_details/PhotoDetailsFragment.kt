package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_details

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.unsplashattestationproject.databinding.FragmentPhotoDetailsBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoItemLoader
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PhotoDetailsFragment : Fragment() {

    private var _binding: FragmentPhotoDetailsBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}