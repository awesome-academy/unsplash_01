package com.sun.unsplash_01.ui.detail

import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.sun.unsplash_01.R
import com.sun.unsplash_01.data.model.PhotoDetail
import com.sun.unsplash_01.data.source.local.sqlite.entity.ImageLocal
import com.sun.unsplash_01.databinding.FragmentDetailBinding
import com.sun.unsplash_01.extensions.hideStatusBar
import com.sun.unsplash_01.extensions.toGone
import com.sun.unsplash_01.extensions.toVisible
import com.sun.unsplash_01.utils.Status
import com.sun.unsplash_01.utils.imagedownload.DownloadImage
import org.koin.android.ext.android.inject

class PhotoDetailFragment : Fragment() {

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
        binding.imageButtonDownload.setOnClickListener {
            photoDetailViewModel.getUrlImage()?.let { urlImage ->
                DownloadImage.newInstance(requireActivity()).downloadImage(urlImage)
            }
        }
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
                    urlPhoto.thumb,
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

        fun newInstance(id: String?) = PhotoDetailFragment().apply {
            arguments = bundleOf(BUNDLE_PHOTO_ID to id)
        }
    }
}
