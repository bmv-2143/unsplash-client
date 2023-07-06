package com.example.unsplashattestationproject.presentation.bottom_navigation.photos_in_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.unsplashattestationproject.databinding.FragmentPhotosInCollectionBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosInCollectionFragment : Fragment() {

    private var _binding: FragmentPhotosInCollectionBinding? = null
    private val binding get() = _binding!!
    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()
    private lateinit var photosInCollectionAdapter: PhotoInCollectionPagedAdapter

    private val photosInCollectionViewModel: PhotosInCollectionViewModel by viewModels()

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
        initAdapter()
        observerPhotosPagedFlow()
    }

    private fun initAdapter() {
        photosInCollectionAdapter = PhotoInCollectionPagedAdapter(
            activityViewModel.selectedCollection,
            ::onPhotoItemClick
        )
        binding.fragmentPhotosInCollectionRecyclerView.adapter = photosInCollectionAdapter
    }

    private fun onPhotoItemClick(photo : PhotoListItemUiModel) {
        Toast.makeText(requireContext(), "Photo clicked $photo", Toast.LENGTH_SHORT).show()
    }

    private fun observerPhotosPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photosInCollectionViewModel.getPhotosPagedFlow().collectLatest { photosPage ->
                    photosInCollectionAdapter.submitData(photosPage)
                }
            }
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
