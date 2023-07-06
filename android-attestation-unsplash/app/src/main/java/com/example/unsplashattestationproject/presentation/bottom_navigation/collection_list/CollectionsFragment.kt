package com.example.unsplashattestationproject.presentation.bottom_navigation.collection_list

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
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unsplashattestationproject.data.dto.collections.UnsplashCollection
import com.example.unsplashattestationproject.databinding.FragmentCollectionsListBinding
import com.example.unsplashattestationproject.log.TAG
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CollectionsFragment : Fragment() {

    private var _binding: FragmentCollectionsListBinding? = null
    private val binding get() = _binding!!
    private val collectionsViewModel: CollectionsViewModel by viewModels()
    private val collectionsAdapter = CollectionPagedAdapter(::onCollectionItemClick)

    private fun onCollectionItemClick(unsplashCollection: UnsplashCollection) {
        Log.e(TAG, "onCollectionItemClick: $unsplashCollection")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionsListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViewLayoutManager()
        binding.fragmentCollectionsRecyclerView.adapter = collectionsAdapter
        observerCollections()
        observeLoadState()
    }

    private fun setupRecyclerViewLayoutManager() {
        val collectionsAdapterLayoutManager = LinearLayoutManager(context)
        binding.fragmentCollectionsRecyclerView.layoutManager = collectionsAdapterLayoutManager
    }

    private fun observerCollections() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectionsViewModel.collectionsPagedFlow.collectLatest { pagingData ->
                    collectionsAdapter.submitData(pagingData)
                }
            }
        }
    }

    private fun observeLoadState() {
        collectionsAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading) {
                binding.fragmentCollectionsProgressBar.visibility = View.VISIBLE
                binding.fragmentCollectionsRecyclerView.visibility = View.GONE
            } else {
                binding.fragmentCollectionsProgressBar.visibility = View.GONE
                binding.fragmentCollectionsRecyclerView.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}