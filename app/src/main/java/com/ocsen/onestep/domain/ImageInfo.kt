package com.ocsen.onestep.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.SerialName

@Parcelize
data class ImageInfo(
    @SerialName("path")
    val path: String = "",
    @SerialName("title")
    val title: String = "",
    @SerialName("desc")
    val desc: String = "",
    @SerialName("lat")
    var lat: Double = 0.0,
    @SerialName("lon")
    var lon: Double = 0.0,
) : Parcelable