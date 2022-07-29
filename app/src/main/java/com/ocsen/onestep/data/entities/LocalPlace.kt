package com.example.data.local.entities

import kotlinx.android.parcel.RawValue

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ocsen.onestep.Utils.DateTimeUtils
import kotlinx.android.parcel.Parcelize

import kotlinx.serialization.SerialName
import java.util.*

@Entity(tableName = "LocalPlaceTable")
@Parcelize
data class LocalPlace(
    @PrimaryKey
    @SerialName("id")
    val id: String = UUID.randomUUID().toString(),
    @SerialName("timestamp")
    val timestamp: Long = System.currentTimeMillis(),
    @SerialName("title")
    val title: String = "",

    @SerialName("desc")
    val desc: String = "",
) : Parcelable

