package com.sun.unsplash_01.data.repository

import com.sun.unsplash_01.data.source.PhotoDataSource
import com.sun.unsplash_01.data.source.local.sqlite.entity.ImageLocal
import com.sun.unsplash_01.utils.Constant
import com.sun.unsplash_01.utils.Constant.DEFAULT_PAGE

class PhotoRepository(
    private val remote: PhotoDataSource.Remote,
    private val local: PhotoDataSource.Local
) {

    suspend fun getCollections(page: Int = DEFAULT_PAGE) = remote.getCollections(page = page)

    suspend fun getPhotosCollection(
        id: String,
        page: Int = DEFAULT_PAGE
    ) = remote.getPhotosCollection(id = id, page = page)

    suspend fun getTopics(page: Int = Constant.DEFAULT_PAGE) = remote.getTopics(page)

    suspend fun getRandomPhotos() = remote.getRandomPhotos()

    suspend fun getPhotoDetail(id: String?) = remote.getPhotoDetail(id)

    suspend fun insertImage(imageLocal: ImageLocal) = local.insertImage(imageLocal)

    suspend fun deleteImage(imageLocal: ImageLocal) = local.deleteImage(imageLocal)

    suspend fun alreadyFavorite(id: String?) = local.getImage(id)

    suspend fun getImages() = local.getImages()

    suspend fun getPhotosTopic(
        id: String,
        page: Int = DEFAULT_PAGE
    ) = remote.getPhotosTopics(id = id, page = page)
}
