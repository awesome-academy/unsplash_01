package com.sun.unsplash_01.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.unsplash_01.data.model.PhotoDetail
import com.sun.unsplash_01.data.repository.PhotoRepository
import com.sun.unsplash_01.data.source.local.sqlite.entity.ImageLocal
import com.sun.unsplash_01.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception

class PhotoDetailViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel() {

    private val _photoDetailLiveData = MutableLiveData<Resource<PhotoDetail>>()
    val photoDetailLiveData: MutableLiveData<Resource<PhotoDetail>>
        get() = _photoDetailLiveData

    private val _isFavorite = MutableLiveData<Resource<Boolean>>()
    val isFavorite: MutableLiveData<Resource<Boolean>>
        get() = _isFavorite

    fun getPhotoDetail(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            _photoDetailLiveData.postValue(Resource.loading(null))
            try {
                _photoDetailLiveData.postValue(
                    Resource.success(photoRepository.getPhotoDetail(id))
                )
            } catch (ex: Exception) {
                _photoDetailLiveData.postValue(Resource.error(null, ex.toString()))
            }
        }
    }

    fun insertImage(imageLocal: ImageLocal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                photoRepository.insertImage(imageLocal)
                _isFavorite.postValue(Resource.success(true))
            } catch (ex: Exception) {
                _isFavorite.postValue(Resource.error(null, ex.toString()))
            }
        }
    }

    fun deleteImage(imageLocal: ImageLocal) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                photoRepository.deleteImage(imageLocal)
                _isFavorite.postValue(Resource.success(false))
            } catch (ex: Exception) {
                _isFavorite.postValue(Resource.error(null, ex.toString()))
            }
        }
    }

    fun alreadyFavorite(id: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (photoRepository.alreadyFavorite(id).urlImage.isNullOrEmpty()) {
                    _isFavorite.postValue(Resource.success(false))
                } else
                    _isFavorite.postValue(Resource.success(true))
            } catch (ex: Exception) {
                _isFavorite.postValue(Resource.error(null, ex.toString()))
            }
        }
    }

    fun getUrlImage() = _photoDetailLiveData.value?.data?.urlPhoto?.small
}
