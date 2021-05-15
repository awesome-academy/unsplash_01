package com.sun.unsplash_01.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Topic(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("cover_photo")
    val photo: PhotoCollection,
) : Parcelable
