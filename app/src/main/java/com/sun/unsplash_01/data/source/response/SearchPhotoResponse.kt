package com.sun.unsplash_01.data.source.response

import com.google.gson.annotations.SerializedName
import com.sun.unsplash_01.data.model.PhotoCollection

data class SearchPhotoResponse(
    @SerializedName("results")
    val photos: MutableList<PhotoCollection> = arrayListOf()
)
