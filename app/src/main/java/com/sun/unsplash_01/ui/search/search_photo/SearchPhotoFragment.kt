package com.sun.unsplash_01.ui.search.search_photo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.unsplash_01.databinding.FragmentSearchPhotoBinding
import com.sun.unsplash_01.ui.photo_collection.PhotoCollectionAdapter
import com.sun.unsplash_01.utils.Status
import org.koin.android.ext.android.inject

class SearchPhotoFragment : Fragment() {

    private lateinit var binding: FragmentSearchPhotoBinding
    private val searchPhotoViewModel: SearchPhotoViewModel by inject()
    private val photoAdapter by lazy { PhotoCollectionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchPhotoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding()
        handleEvent()
        registerObserver()
    }

    private fun handleEvent() {
        photoAdapter.setOnClickItem {
        }
    }

    fun searchPhoto(keyWord: String) {
        searchPhotoViewModel.searchFirstTime(keyWord)
    }

    private fun registerObserver() {
        searchPhotoViewModel.resource.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    photoAdapter.submitList(it.data?.value?.toMutableList())
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

    fun binding() {
        binding.apply {
            lifecycleOwner = this@SearchPhotoFragment
            viewModel = searchPhotoViewModel
            (recyclerViewPhotos.layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            adapter = photoAdapter
        }
    }

    companion object {
        fun newInstance() = SearchPhotoFragment()
    }
}
