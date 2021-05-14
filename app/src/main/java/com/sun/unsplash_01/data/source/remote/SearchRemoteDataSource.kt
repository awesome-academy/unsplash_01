package com.sun.unsplash_01.data.source.remote

class SearchRemoteDataSource(private val apiService: APIService) : SearchDataSource.Remote {

    override suspend fun searchCollection(
        keyword: String,
        page: Int
    ) = apiService.searchCollection(keyword, page)

    override suspend fun searchPhoto(
        keyword: String,
        page: Int
    ) = apiService.searchPhoto(keyword, page)
}
