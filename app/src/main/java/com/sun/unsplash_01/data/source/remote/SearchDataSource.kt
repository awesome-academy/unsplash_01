package com.sun.unsplash_01.data.source.remote

import com.sun.unsplash_01.data.source.response.SearchCollectionResponse
import com.sun.unsplash_01.data.source.response.SearchPhotoResponse

interface SearchDataSource {

    interface Remote {

        suspend fun searchCollection(keyword: String, page: Int): SearchCollectionResponse

        suspend fun searchPhoto(keyword: String, page: Int): SearchPhotoResponse
    }

    interface Local {
    }
}
