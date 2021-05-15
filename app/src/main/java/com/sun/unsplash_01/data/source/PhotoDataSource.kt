package com.sun.unsplash_01.data.source

import com.sun.unsplash_01.data.model.Collection
import com.sun.unsplash_01.data.model.PhotoCollection
import com.sun.unsplash_01.data.model.PhotoDetail
import com.sun.unsplash_01.data.model.Topic
import com.sun.unsplash_01.data.source.local.sqlite.entity.ImageLocal

class PhotoDataSource {

    interface Local {
        suspend fun getImages(): List<ImageLocal>

        suspend fun insertImage(imageLocal: ImageLocal)

        suspend fun deleteImage(imageLocal: ImageLocal)

        suspend fun getImage(imageId: String?): ImageLocal
    }

    interface Remote {
        suspend fun getCollections(page: Int): MutableList<Collection>

        suspend fun getPhotosCollection(id: String, page: Int): MutableList<PhotoCollection>

        suspend fun getTopics(page: Int): MutableList<Topic>

        suspend fun getRandomPhotos(): MutableList<PhotoCollection>

        suspend fun getPhotoDetail(id: String?): PhotoDetail

        suspend fun getPhotosTopics(id: String, page: Int): MutableList<PhotoCollection>
    }
}
