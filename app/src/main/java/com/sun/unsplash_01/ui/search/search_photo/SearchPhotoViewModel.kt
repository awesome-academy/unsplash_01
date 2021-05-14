package com.sun.unsplash_01.ui.search.search_photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sun.unsplash_01.data.model.PhotoCollection
import com.sun.unsplash_01.data.repository.SearchRepository
import com.sun.unsplash_01.extensions.plusAssign
import com.sun.unsplash_01.utils.Constant.DEFAULT_PAGE
import com.sun.unsplash_01.utils.Constant.DEFAULT_STRING
import com.sun.unsplash_01.utils.LoadMoreRecyclerViewListener
import com.sun.unsplash_01.utils.RefreshRecyclerViewListener
import com.sun.unsplash_01.utils.Resource
import kotlinx.coroutines.launch

class SearchPhotoViewModel(
    private val searchRepository: SearchRepository
) : ViewModel(),
    LoadMoreRecyclerViewListener,
    RefreshRecyclerViewListener {

    private var currentPosition = DEFAULT_PAGE
    private var keyword: String = DEFAULT_STRING
    private val _resource = MutableLiveData<Resource<LiveData<MutableList<PhotoCollection>>>>()
    val resource: LiveData<Resource<LiveData<MutableList<PhotoCollection>>>>
        get() = _resource

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _photos = MutableLiveData<MutableList<PhotoCollection>>(mutableListOf())
    val photos: LiveData<MutableList<PhotoCollection>>
        get() = _photos

    fun searchFirstTime(keyword: String) {
        this.keyword = keyword
        _photos.value?.clear()
        searchPhotos(keyword)
    }

    fun searchPhotos(keyword: String) {
        viewModelScope.launch {
            try {
                _photos.plusAssign(
                    searchRepository.searchPhoto(
                        keyword, currentPosition
                    ).photos
                )
                _resource.postValue(Resource.success(data = photos))
                currentPosition++
                _isLoading.value = false
            } catch (e: Exception) {
                _resource.postValue(Resource.error(data = null, message = e.message.toString()))
            }
        }
    }

    override fun onLoadData() {
        _isLoading.value = true
        searchPhotos(keyword)
    }

    override fun onRefresh() {
        _photos.value?.clear()
        currentPosition = DEFAULT_PAGE
        searchPhotos(keyword)
    }
}
