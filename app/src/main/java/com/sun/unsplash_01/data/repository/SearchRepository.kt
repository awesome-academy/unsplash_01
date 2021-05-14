package com.sun.unsplash_01.data.repository

import com.sun.unsplash_01.data.source.remote.SearchDataSource

class SearchRepository(private val remote: SearchDataSource.Remote) {

    suspend fun searchCollection(
        keyword: String,
        page: Int
    ) = remote.searchCollection(keyword, page)


    suspend fun searchPhoto(
        keyword: String,
        page: Int
    ) = remote.searchPhoto(keyword, page)
}
