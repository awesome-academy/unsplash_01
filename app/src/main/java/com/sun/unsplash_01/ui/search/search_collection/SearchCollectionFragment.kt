package com.sun.unsplash_01.ui.search.search_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.unsplash_01.R
import com.sun.unsplash_01.databinding.FragmentSearchCollectionBinding
import com.sun.unsplash_01.ui.collection.CollectionAdapter
import com.sun.unsplash_01.ui.collection.CollectionFragment
import com.sun.unsplash_01.ui.photo_collection.PhotoCollectionFragment
import com.sun.unsplash_01.utils.Status
import org.koin.android.ext.android.inject

class SearchCollectionFragment : Fragment() {

    private lateinit var binding: FragmentSearchCollectionBinding
    private val searchCollectionViewModel: SearchCollectionViewModel by inject()
    private val collectionAdapter by lazy { CollectionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding()
        handleEvent()
        registerObserver()
    }

    private fun handleEvent() {
        collectionAdapter.setOnClickItem {
            findNavController().navigate(
                R.id.photoCollectionFragment,
                bundleOf(PhotoCollectionFragment.BUNDLE_COLLECTION to it)
            )
        }
    }

    private fun registerObserver() {
        searchCollectionViewModel.resource.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    collectionAdapter.submitList(it.data?.value?.toMutableList())
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.swipeRefresh.isRefreshing = false
                }
                Status.LOADING -> {
                }
            }
        })
    }

    fun searchCollection(keyWord: String) {
        searchCollectionViewModel.searchFirstTime(keyWord)
    }

    fun binding() {
        binding.apply {
            lifecycleOwner = this@SearchCollectionFragment
            viewModel = searchCollectionViewModel
            (recyclerViewCollections.layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            adapter = collectionAdapter
        }
    }

    companion object {
        fun newInstance() = SearchCollectionFragment()
    }
}
