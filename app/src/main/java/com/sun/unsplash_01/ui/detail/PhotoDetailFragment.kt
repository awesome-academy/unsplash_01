package com.sun.unsplash_01.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sun.unsplash_01.R
import com.sun.unsplash_01.data.model.PhotoDetail
import com.sun.unsplash_01.data.source.local.sqlite.entity.ImageLocal
import com.sun.unsplash_01.databinding.FragmentDetailBinding
import com.sun.unsplash_01.extensions.hideStatusBar
import com.sun.unsplash_01.extensions.showToast
import com.sun.unsplash_01.extensions.toGone
import com.sun.unsplash_01.extensions.toVisible
import com.sun.unsplash_01.utils.ErrorMessage
import com.sun.unsplash_01.utils.Status
import com.sun.unsplash_01.widgets.CustomImageFilterView
import org.koin.android.ext.android.inject

class PhotoDetailFragment : Fragment() {

    private var isScroll = false
    private var isFavorite = false
    private var photoID: String? = null
    private var imageFavorite: ImageLocal? = null
    private lateinit var binding: FragmentDetailBinding
    private val photoDetailViewModel: PhotoDetailViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            photoID = it.getString(BUNDLE_PHOTO_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().hideStatusBar()
        binding = DataBindingUtil.inflate<FragmentDetailBinding>(
            inflater,
            R.layout.fragment_detail,
            container,
            false
        ).apply {
            lifecycleOwner = this@PhotoDetailFragment
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        handleEvent()
    }

    private fun handleEvent() {
        binding.imageButtonFavorite.setOnClickListener {
            if (!isFavorite) {
                imageFavorite?.let { imageLocalData ->
                    photoDetailViewModel.insertImage(imageLocalData)
                }
            } else {
                imageFavorite?.let { imageLocalData ->
                    photoDetailViewModel.deleteImage(imageLocalData)
                }
            }
        }
        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
        handleFilter()
        handleDraw()
    }

    private fun handleDraw() {
        binding.run {
            imageButtonDraw.setOnClickListener {
                if (isScroll) {
                    isScroll = false
                    CustomImageFilterView.isDraw = false
                    scrollViewDetail.setScrollingEnabled(true)
                    it.setBackgroundColor(resources.getColor(R.color.white))
                    requireContext().showToast(getString(R.string.unlock_scroll_view))
                } else {
                    isScroll = true
                    CustomImageFilterView.isDraw = true
                    scrollViewDetail.setScrollingEnabled(false)
                    it.setBackgroundColor(resources.getColor(R.color.color_gray))
                    requireContext().showToast(getString(R.string.lock_scroll_view))
                }
            }
        }
    }

    private fun handleFilter() = with(binding) {
        seekBarSaturation.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                imageViewImageDetail.saturation = (progress / MAX_PERCENT) * NUMBER_TWO
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekBarContrast.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                imageViewImageDetail.contrast = (progress / MAX_PERCENT) * NUMBER_TWO
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        seekBarWarmth.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                imageViewImageDetail.warmth = (progress / MAX_PERCENT) * NUMBER_TWO
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
    }

    private fun setupObserver() = with(photoDetailViewModel) {
        getPhotoDetail(photoID)
        alreadyFavorite(photoID)
        photoDetailLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.LOADING -> {
                    binding.progressLayout.toVisible()
                }
                Status.SUCCESS -> {
                    it.data.run {
                        binding.photoDetail = this
                        setData(this)
                    }
                    binding.progressLayout.toGone()
                }
                Status.ERROR -> {
                    binding.progressLayout.toGone()
                    when(it.message.toString()) {
                        ErrorMessage.RATE_LIMIT_EXCEEDED -> {
                            requireContext().showToast(getString(R.string.rate_limit_exceeded))
                        }
                        ErrorMessage.NO_NETWORK_CONNECTION -> {
                            requireContext().showToast(getString(R.string.no_network_connection))
                        }
                    }
                }
            }
        })
        isFavorite.observe(viewLifecycleOwner, {
            it.data?.let { isFavoriteData ->
                this@PhotoDetailFragment.isFavorite = isFavoriteData
            }
            when (it.status) {
                Status.LOADING -> {
                }
                Status.SUCCESS -> {
                    if (it.data == true) {
                        binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite_checked)
                    } else {
                        binding.imageButtonFavorite.setImageResource(R.drawable.ic_favorite)
                    }
                }
                Status.ERROR -> {
                }
            }
        })
    }

    private fun setData(data: PhotoDetail?) {
        data?.apply {
            id?.let {
                imageFavorite = ImageLocal(
                    it,
                    urlPhoto.regular,
                    authorInformation.name,
                    authorInformation.avatar.smallAvatar,
                    views,
                    likes,
                    downloads
                )
            }
        }
    }

    companion object {
        const val BUNDLE_PHOTO_ID = "BUNDLE_PHOTO_ID"
        private const val MAX_PERCENT = 100F
        private const val NUMBER_TWO = 2

        fun newInstance(id: String?) = PhotoDetailFragment().apply {
            arguments = bundleOf(BUNDLE_PHOTO_ID to id)
        }
    }
}
