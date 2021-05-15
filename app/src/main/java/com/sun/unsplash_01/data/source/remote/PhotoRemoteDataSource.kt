package com.sun.unsplash_01.data.source.remote

import com.sun.unsplash_01.data.model.PhotoDetail
import com.sun.unsplash_01.data.source.PhotoDataSource


class PhotoRemoteDataSource(private val apiService: APIService) : PhotoDataSource.Remote {

    override suspend fun getCollections(page: Int) = apiService.getCollections(page = page)

    override suspend fun getPhotosCollection(id: String, page: Int) =
        apiService.getPhotosCollection(id = id, page = page)

    override suspend fun getTopics(page: Int) = apiService.getTopics(page)

    override suspend fun getRandomPhotos() = apiService.getRandomPhotos()

    override suspend fun getPhotoDetail(id: String?): PhotoDetail = apiService.getPhotoDetail(id)

    override suspend fun getPhotosTopics(id: String, page: Int) =
        apiService.getPhotosTopic(id = id, page = page)
}
