package com.sun.unsplash_01.data.source.response

import com.google.gson.annotations.SerializedName
import com.sun.unsplash_01.data.model.Collection

data class SearchCollectionResponse(
    @SerializedName("results")
    val collections: MutableList<Collection> = arrayListOf()
)
