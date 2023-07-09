package com.example.unsplashattestationproject.presentation.bottom_navigation.liked_photos_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentLikedPhotosBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotoListItemUiModel
import com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list.PhotosPagedAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LikedPhotosFragment : Fragment() {

    private var usernameArgument: String? = null

    private var _binding: FragmentLikedPhotosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LikedPhotosViewModel by viewModels()

    private val photoListAdapter = PhotosPagedAdapter(::onPhotoItemClick)

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    private fun onPhotoItemClick(photo: PhotoListItemUiModel) {
        val navController = findNavController()
        activityViewModel.selectedFromPhotoList = photo
        navController.navigate(R.id.photoDetailsFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            usernameArgument = it.getString(ARG_USERNAME)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLikedPhotosBinding.inflate(inflater, container, false)
        initAdapter()
        return binding.root
    }

    private fun initAdapter() {
        binding.fragmentLikedPhotosRecyclerView.adapter =
            photoListAdapter
        binding.fragmentLikedPhotosRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usernameArgument?.let {
            viewModel.loadLikedPhotos(it)
            observerPhotosPagedFlow()
        }
    }

    private fun observerPhotosPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.likedPhotos.collectLatest { photosPage ->
                    photoListAdapter.submitData(photosPage)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_USERNAME = "arg_username"

        @JvmStatic
        fun newInstance(username: String) =
            LikedPhotosFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_USERNAME, username)
                }
            }
    }
}