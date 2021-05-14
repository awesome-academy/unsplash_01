package com.sun.unsplash_01.data.repository

import com.sun.unsplash_01.data.source.PhotoDataSource
import com.sun.unsplash_01.utils.Constant
import com.sun.unsplash_01.utils.Constant.DEFAULT_PAGE

class PhotoRepository(private val remote: PhotoDataSource.Remote) {

    suspend fun getCollections(page: Int = DEFAULT_PAGE) = remote.getCollections(page = page)

    suspend fun getPhotosCollection(
        id: String,
        page: Int = DEFAULT_PAGE
    ) = remote.getPhotosCollection(id = id, page = page)

    suspend fun getTopics(page: Int = Constant.DEFAULT_PAGE) = remote.getTopics(page)

    suspend fun getRandomPhotos() = remote.getRandomPhotos() 
}
