package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.databinding.FragmentHomeBinding
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val photoListViewModel: PhotoListViewModel by viewModels()
    private val photoListAdapter = PhotosPagedAdapter(::onPhotoItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        updateDummyTextOnFragmentChange(photoListViewModel)
        initRecyclerViewAdapter()

        return binding.root
    }

    private fun initRecyclerViewAdapter() {
        binding.fragmentRickAndMortyCharactersRecyclerView.adapter =
            photoListAdapter
    }

    private fun updateDummyTextOnFragmentChange(photoListViewModel: PhotoListViewModel) {
        val textView: TextView = binding.textHome
        photoListViewModel.dummyText.observe(viewLifecycleOwner) {
            textView.text = it
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPhotosPagedFlow()
    }

    private fun observerPhotosPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoListViewModel.getPhotosPagedFlow().collectLatest {
                    it.let { photosPage ->
                        photoListAdapter.submitData(photosPage)
                    }
                }
            }
        }
    }

    private fun onPhotoItemClick(photo: UnsplashPhoto) {
        Log.e(TAG, "CLICKED: $photo")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}