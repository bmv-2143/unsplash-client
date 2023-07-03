package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentPhotoListBinding
import com.example.unsplashattestationproject.log.TAG
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.utils.SnackbarFactory
import com.example.unsplashattestationproject.utils.NetworkStateChecker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PhotoListFragment : Fragment() {

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val photoListViewModel: PhotoListViewModel by viewModels()
    private val photoListAdapter = PhotosPagedAdapter(::onPhotoItemClick)
    private val searchAdapter = PhotosPagedAdapter(::onPhotoItemClick)

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

    private lateinit var staggeredGridLayoutManager: StaggeredGridLayoutManager

    @Inject
    lateinit var networkStateChecker: NetworkStateChecker

    @Inject
    lateinit var snackbarFactory: SnackbarFactory

    private lateinit var searchMenuProvider: SearchMenuProvider

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoListBinding.inflate(inflater, container, false)

        setupRecyclerViewLayoutManager()
        initAdapter()
        setRecyclerViewScrollListener()
        initSearchMenuProvider()
        addActionBarMenu()
        return binding.root
    }

    private fun initAdapter() {
        if (photoListViewModel.isSearchMode) {
            activateSearchAdapter()
        } else {
            activatePhotoListAdapter()
        }
    }

    private fun initSearchMenuProvider() {
        searchMenuProvider = SearchMenuProvider(
            requireContext(),
            photoListViewModel.currentQuery,
            startSearchAction = { query ->
                activateSearchAdapter()
                photoListViewModel.startSearch(query)
            },
            photoListViewModel::onQueryTextChanged,
            clearSearchAction = {
                photoListViewModel::clearSearchResults
                activatePhotoListAdapter()
            }
        )
    }

    private fun setupRecyclerViewLayoutManager() {
        staggeredGridLayoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        staggeredGridLayoutManager.gapStrategy =
            StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
        binding.fragmentPhotoRecyclerView.layoutManager = staggeredGridLayoutManager

    }

    private fun activatePhotoListAdapter() {
        binding.fragmentPhotoRecyclerView.adapter =
            photoListAdapter
        photoListViewModel.isSearchMode = false
    }

    private fun activateSearchAdapter() {
        binding.fragmentPhotoRecyclerView.adapter =
            searchAdapter
        photoListViewModel.isSearchMode = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPhotosPagedFlow()
        observerPagingAdapterUpdates()
        observerFragmentStateChange()
        observerSearchPagedFlow()
    }

    private fun observerPhotosPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoListViewModel.getPhotosPagedFlow().collectLatest { photosPage ->
                    photoListAdapter.submitData(photosPage)
                }
            }
        }
    }

    private fun observerPagingAdapterUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            photoListAdapter.loadStateFlow.collect { loadStates ->
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

    override fun onResume() {
        super.onResume()
        showNoConnectionSnackbar()
        updateSelectedItemLikes()
    }

    private fun updateSelectedItemLikes() {
        if (activityViewModel.selectedFromPhotoList != null) {
            photoListAdapter.updateItemLikes(activityViewModel.selectedFromPhotoList!!)
        }
    }

    private fun showNoConnectionSnackbar() {
        if (!networkStateChecker.isNetworkAvailable()) {
            snackbarFactory.showSnackbar(
                binding.root,
                getString(R.string.fragment_photo_list_no_internet_msg),
                getString(R.string.fragment_photo_list_not_internet_msg_close)
            )
        }
    }

    private fun setRecyclerViewScrollListener() {
        binding.fragmentPhotoRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = staggeredGridLayoutManager.childCount
                val totalItemCount = staggeredGridLayoutManager.itemCount

                val firstVisibleItemPositions =
                    staggeredGridLayoutManager.findFirstVisibleItemPositions(null)
                if (visibleItemCount + firstVisibleItemPositions[0] >= totalItemCount) {
                    Log.e(TAG, "onScrolled: RETRY")
                    photoListAdapter.retry()
                }
            }
        })
    }

    private fun addActionBarMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(searchMenuProvider, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observerSearchPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoListViewModel.searchResults.collectLatest { searchResults ->
                    searchResults.collectLatest { photosPage ->
                        searchAdapter.submitData(photosPage)
                    }
                }
            }
        }
    }

    private fun onPhotoItemClick(photo: PhotoListItemUiModel) {
        val navController = findNavController()
        activityViewModel.selectedFromPhotoList = photo
        navController.navigate(R.id.photoDetailsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}