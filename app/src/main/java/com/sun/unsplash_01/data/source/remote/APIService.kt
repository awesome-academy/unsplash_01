package com.sun.unsplash_01.data.source.remote

import com.sun.unsplash_01.data.model.Collection
import com.sun.unsplash_01.data.model.PhotoCollection
import com.sun.unsplash_01.data.model.Topic
import com.sun.unsplash_01.data.source.response.SearchCollectionResponse
import com.sun.unsplash_01.data.source.response.SearchPhotoResponse
import com.sun.unsplash_01.utils.Constant
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIService {

    @GET("/collections")
    suspend fun getCollections(
        @Query("page") page: Int
    ): MutableList<Collection>

    @GET("/collections/{id}/photos")
    suspend fun getPhotosCollection(
        @Path("id") id: String,
        @Query("page") page: Int = Constant.DEFAULT_PAGE
    ): MutableList<PhotoCollection>

    @GET("/topics")
    suspend fun getTopics(
        @Query("page") page: Int = Constant.DEFAULT_PAGE
    ): MutableList<Topic>

    @GET("/photos/random")
    suspend fun getRandomPhotos(
        @Query("count") count: Int = Constant.RANDOM_ITEM_COUNT
    ): MutableList<PhotoCollection>

    @GET("search/collections")
    suspend  fun searchCollection(
        @Query("query") keyword: String,
        @Query("page") page: Int = Constant.DEFAULT_PAGE
    ): SearchCollectionResponse

    @GET("search/photos")
    suspend fun searchPhoto(
        @Query("query") keyword: String,
        @Query("page") page: Int = Constant.DEFAULT_PAGE
    ): SearchPhotoResponse
}
