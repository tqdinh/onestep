package com.example.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ocsen.onestep.Utils.DateTimeUtils
import kotlinx.android.parcel.Parcelize
import kotlinx.serialization.SerialName
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "LocalImageTable", primaryKeys = ["id", "place_id"])
@Parcelize
data class LocalImage(
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("place_id")
    val place_id: String,
    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
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