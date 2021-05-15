package com.sun.unsplash_01.ui.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sun.unsplash_01.R
import com.sun.unsplash_01.data.model.PhotoCollection
import com.sun.unsplash_01.databinding.FragmentHomeBinding
import com.sun.unsplash_01.extensions.toGone
import com.sun.unsplash_01.extensions.toVisible
import com.sun.unsplash_01.ui.detail.PhotoDetailFragment
import com.sun.unsplash_01.ui.photo_collection.PhotoCollectionFragment
import com.sun.unsplash_01.utils.Status
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private val images = mutableListOf<PhotoCollection>()
    private val homeViewModel by viewModel<HomeViewModel>()
    private val homeAdapter by lazy {
        HomeSlideAdapter(requireActivity(), images) {
            findNavController().navigate(
                R.id.imageDetailFragment,
                bundleOf(PhotoDetailFragment.BUNDLE_PHOTO_ID to it)
            )
        }
    }
    private val homeTopicsAdapter by lazy {
        HomeTopicsAdapter {
            findNavController().navigate(
                R.id.photoCollectionFragment,
                bundleOf(PhotoCollectionFragment.BUNDLE_TOPIC to it)
            )
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBinDing()
        initSlide()
        initTopics()
    }

    private fun initBinDing() {
        binding.apply {
            lifecycleOwner = this@HomeFragment
            adapterTopics = homeTopicsAdapter
            viewModel = homeViewModel
        }
    }

    private fun initSlide() {
        homeViewModel.photos.observe(viewLifecycleOwner, {
            images.apply {
                clear()
                addAll(it)
            }
            binding.viewPagerSlide.apply {
                if (images.size == 0) toGone() else toVisible()
            }
            homeAdapter.notifyDataSetChanged()
        })
        binding.viewPagerSlide.apply {
            adapter = homeAdapter
            offscreenPageLimit = OFF_SCREEN_PAGE_LIMIT
            pageMargin = ITEM_DISTANCE
        }
        var currentPage = 0
        val handler = Looper.myLooper()?.let { Handler(it) }
        val runnable = Runnable {
            if (currentPage > images.size) {
                currentPage = 0
            }
            binding.viewPagerSlide.setCurrentItem(currentPage++, true)
        }
        Timer().run {
            schedule(object : TimerTask() {
                override fun run() {
                    handler?.post(runnable)
                }
            }, DELAY_TIME, DELAY_NEXT_ITEM)
        }
    }


    private fun initTopics() {
        homeViewModel.topics.observe(viewLifecycleOwner, {
            homeTopicsAdapter.submitList(it.toMutableList())
        })
        homeViewModel.resource.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.swipeRefreshHome.isRefreshing = false
                }
                Status.ERROR -> {
                    binding.swipeRefreshHome.isRefreshing = false
                }
                Status.LOADING -> {
                }
            }
        })
    }

    companion object {
        private const val DELAY_TIME = 2000L
        private const val DELAY_NEXT_ITEM = 3000L
        private const val ITEM_DISTANCE = 30
        private const val OFF_SCREEN_PAGE_LIMIT = 5

        fun newInstance() = HomeFragment()
    }
}
