package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplashattestationproject.data.dto.photos.UnsplashPhoto
import com.example.unsplashattestationproject.databinding.FragmentPhotoListBinding
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val photoListViewModel: PhotoListViewModel by viewModels()
    private val photoListAdapter = PhotosPagedAdapter(::onPhotoItemClick)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false)

        setupRecyclerViewLayoutManager()
        initRecyclerViewAdapter()

        return binding.root
    }

    private fun setupRecyclerViewLayoutManager() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        staggeredGridLayoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.fragmentPhotoRecyclerView.layoutManager = staggeredGridLayoutManager

    }

    private fun initRecyclerViewAdapter() {
        binding.fragmentPhotoRecyclerView.adapter =
            photoListAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPhotosPagedFlow()
        observerPagingAdapterUpdates()
        observerFragmentStateChange()
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

    private fun observerPagingAdapterUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            photoListAdapter.loadStateFlow.collectLatest { loadStates ->
                photoListViewModel.handleAdapterUpdates(loadStates)
            }
        }
    }

    private fun observerFragmentStateChange() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoListViewModel.uiStatesFlow.collect { state ->
                    state.activate(binding)
                }
            }
        }
    }

    private fun onPhotoItemClick(photo: UnsplashPhoto) {
        Log.e(TAG, "CLICKED: ${photo.id}}")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}