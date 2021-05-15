package com.sun.unsplash_01.ui.photo_collection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.sun.unsplash_01.R
import com.sun.unsplash_01.data.model.Collection
import com.sun.unsplash_01.databinding.FragmentPhotoCollectionBinding
import com.sun.unsplash_01.ui.collection.CollectionFragment.Companion.BUNDLE_COLLECTION
import com.sun.unsplash_01.ui.detail.PhotoDetailFragment
import com.sun.unsplash_01.utils.Status
import org.koin.android.ext.android.inject

class PhotoCollectionFragment : Fragment() {

    lateinit var binding: FragmentPhotoCollectionBinding
    private val photoCollectionViewModel: PhotoCollectionViewModel by inject()
    private val photoCollectionAdapter by lazy { PhotoCollectionAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding()
        handleEvent()
        registerObserver()
    }

    private fun handleEvent() {
        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }
        photoCollectionAdapter.setOnClickItem {
            findNavController().navigate(
                R.id.imageDetailFragment,
                bundleOf(PhotoDetailFragment.BUNDLE_PHOTO_ID to it.id)
            )
        }
    }

    override fun onStart() {
        super.onStart()
        arguments?.getParcelable<Collection>(BUNDLE_COLLECTION)?.let {
            photoCollectionViewModel.fetchCollections(it.id)
        }
    }

    private fun registerObserver() {
        photoCollectionViewModel.resource.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    photoCollectionAdapter.submitList(it.data?.value?.toMutableList())
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
            lifecycleOwner = this@PhotoCollectionFragment
            viewModel = photoCollectionViewModel
            (recyclerViewPhotoCollections.layoutManager as StaggeredGridLayoutManager).gapStrategy =
                StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            adapter = photoCollectionAdapter
        }
    }

    companion object {
        fun newInstance() = PhotoCollectionFragment()
    }
}
