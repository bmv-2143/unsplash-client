package com.example.unsplashattestationproject.presentation.bottom_navigation.photo_list

import android.os.Bundle
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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.unsplashattestationproject.R
import com.example.unsplashattestationproject.databinding.FragmentPhotoListBinding
import com.example.unsplashattestationproject.presentation.bottom_navigation.BottomNavigationActivityViewModel
import com.example.unsplashattestationproject.presentation.utils.SnackbarFactory
import com.example.unsplashattestationproject.utils.NetworkStateChecker
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class PhotoListFragment @AssistedInject constructor(
    @Assisted private val layoutManagerFactory: () -> RecyclerView.LayoutManager) :
    Fragment() {

    constructor() : this({ StaggeredGridLayoutManager(
        2, StaggeredGridLayoutManager.VERTICAL) })

    @AssistedFactory
    fun interface Factory {
        fun create(layoutManagerFactory: () -> RecyclerView.LayoutManager): PhotoListFragment
    }

    private var _binding: FragmentPhotoListBinding? = null
    private val binding get() = _binding!!

    private val photoListViewModel: PhotoListViewModel by viewModels()
    private var photoListAdapter : PhotosPagedAdapter? = null
    private val searchAdapter = PhotosPagedAdapter(::onPhotoItemClick)

    private val activityViewModel: BottomNavigationActivityViewModel by activityViewModels()

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

        photoListAdapter = PhotosPagedAdapter(::onPhotoItemClick)
        setupRecyclerViewLayoutManager()
        initAdapter()
        setRecyclerViewScrollListener()
        initSearchMenuProvider()
        addActionBarMenu()
        return binding.root
    }

    private fun initAdapter() {
        if (photoListViewModel.isSearchOpened) {
            activateSearchAdapter()
        } else {
            activatePhotoListAdapter()
        }
    }

    private fun initSearchMenuProvider() {
        searchMenuProvider = SearchMenuProvider(
            photoListViewModel.currentQuery,

            onMenuSearchExpanded = photoListViewModel::onSearchOpened,

            onQueryTextChanged = {
                photoListViewModel.onQueryTextChanged(it)
            },

            onSearchQuerySubmit = { query ->
                activateSearchAdapter()
                photoListViewModel.onSearchSubmitted(query)
            },

            onMenuSearchCollapsed = {
                activatePhotoListAdapter()
                photoListViewModel.onSearchClosed()
            },
        )
    }


    private fun setupRecyclerViewLayoutManager() {
        binding.fragmentPhotoRecyclerView.layoutManager = layoutManagerFactory()
    }

    private fun activatePhotoListAdapter() {
        binding.fragmentPhotoRecyclerView.adapter =
            photoListAdapter
    }

    private fun activateSearchAdapter() {
        binding.fragmentPhotoRecyclerView.adapter =
            searchAdapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observerPhotosPagedFlow()
        observerPagingAdapterUpdates()
        observerFragmentStateChange()
        observerSearchPagedFlow()
        observeLoadState()
    }

    private fun observerPhotosPagedFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                photoListViewModel.getPhotosPagedFlow().collectLatest { photosPage ->
                    photoListAdapter?.submitData(photosPage)
                }
            }
        }
    }

    private fun observerPagingAdapterUpdates() {
        viewLifecycleOwner.lifecycleScope.launch {
            photoListAdapter?.loadStateFlow?.collect { loadStates ->
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
        showNoConnectionSnackbarIfRequired()
        updateSelectedItemLikes()
    }

    private fun updateSelectedItemLikes() {
        if (activityViewModel.selectedFromPhotoList != null) {
            photoListAdapter?.updateItemLikes(activityViewModel.selectedFromPhotoList!!)
        }
    }

    private fun showNoConnectionSnackbarIfRequired() {
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
                val layoutManager = binding.fragmentPhotoRecyclerView.layoutManager!!
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount

                val firstVisibleItemPosition = when (layoutManager) {
                    is StaggeredGridLayoutManager -> layoutManager.findFirstVisibleItemPositions(
                        null
                    )[0]

                    is LinearLayoutManager -> layoutManager.findFirstVisibleItemPosition()
                    else -> return
                }
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount) {
                    photoListAdapter?.retry()
                    showNoConnectionSnackbarIfRequired()
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
                photoListViewModel.searchResults.collectLatest { photosPage ->
                    searchAdapter.submitData(photosPage)
                }
            }
        }
    }

    private fun observeLoadState() {
        photoListAdapter?.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.fragmentPhotoProgressBar.visibility = View.VISIBLE
                binding.fragmentPhotoRecyclerView.visibility = View.GONE
            } else {
                binding.fragmentPhotoProgressBar.visibility = View.GONE
                binding.fragmentPhotoRecyclerView.visibility = View.VISIBLE
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
        binding.fragmentPhotoRecyclerView.adapter = null // fix memory leak
        _binding = null
        photoListAdapter = null
    }
}