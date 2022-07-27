package com.ocsen.onestep.domain

import kotlinx.serialization.SerialName
import java.util.*
import kotlin.collections.ArrayList

data class PlaceInfo(
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("lat")
    var lat: Double = 0.0,
    @SerialName("lon")
    var lon: Double = 0.0,
    @SerialName("img")
    val imgs: ArrayList<ImageInfo> = ArrayList()
)