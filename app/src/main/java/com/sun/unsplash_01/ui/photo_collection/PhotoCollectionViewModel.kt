package com.sun.unsplash_01.ui.photo_collection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.unsplash_01.data.model.PhotoCollection
import com.sun.unsplash_01.data.repository.PhotoRepository
import com.sun.unsplash_01.extensions.plusAssign
import com.sun.unsplash_01.utils.Constant.API_COLLECTION
import com.sun.unsplash_01.utils.Constant.DEFAULT_ID
import com.sun.unsplash_01.utils.Constant.DEFAULT_PAGE
import com.sun.unsplash_01.utils.LoadMoreRecyclerViewListener
import com.sun.unsplash_01.utils.RefreshRecyclerViewListener
import com.sun.unsplash_01.utils.Resource
import kotlinx.coroutines.launch

class PhotoCollectionViewModel(
    private val photoRepository: PhotoRepository
) : ViewModel(),
    LoadMoreRecyclerViewListener,
    RefreshRecyclerViewListener {

    private var currentPosition = DEFAULT_PAGE
    private var id: String = DEFAULT_ID
    private var api: String = DEFAULT_ID
    private val _resource = MutableLiveData<Resource<LiveData<MutableList<PhotoCollection>>>>()
    val resource: LiveData<Resource<LiveData<MutableList<PhotoCollection>>>>
        get() = _resource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _photoCollection = MutableLiveData<MutableList<PhotoCollection>>(mutableListOf())
    val photoCollection: LiveData<MutableList<PhotoCollection>>
        get() = _photoCollection

    fun fetchCollections(id: String, api: String) {
        viewModelScope.launch {
            try {
                this@PhotoCollectionViewModel.id = id
                this@PhotoCollectionViewModel.api = api
                _photoCollection.plusAssign(
                    if (api == API_COLLECTION) {
                        photoRepository.getPhotosCollection(id, currentPosition)
                    } else {
                        photoRepository.getPhotosTopic(id, currentPosition)
                    }
                )
                _resource.postValue(Resource.success(data = photoCollection))
                currentPosition++
                _isLoading.value = false
            } catch (e: Exception) {
                _resource.postValue(Resource.error(data = null, message = e.message.toString()))
            }
        }
    }

    override fun onLoadData() {
        _isLoading.value = true
        fetchCollections(id, api)
    }

    override fun onRefresh() {
        _photoCollection.value?.clear()
        currentPosition = DEFAULT_PAGE
        fetchCollections(id, api)
    }
}
