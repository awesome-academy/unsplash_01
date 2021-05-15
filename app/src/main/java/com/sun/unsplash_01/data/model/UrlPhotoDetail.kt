package com.sun.unsplash_01.data.model

import com.google.gson.annotations.SerializedName

data class UrlPhotoDetail(
    @SerializedName("small")
    val small: String?,
    @SerializedName("thumb")
    val thumb: String?,
    @SerializedName("regular")
    val regular: String?
)
