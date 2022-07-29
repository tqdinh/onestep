package com.ocsen.onestep.data.repositories

import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace

interface PlaceAndImageRepository {
    fun insertPlace(place: LocalPlace)
    fun updatePlace(place: LocalPlace)
    fun deletePlace(id:String)
    fun getListOfPlace():List<LocalPlace>

    fun insertImageIdToPlace(placeId:String, imgId:String)
    fun updateImageIdToPlace()
    fun deleteImageIdOnPlace()
    fun getListOfImageIdOnPlace()

    fun insertImage(localImage: LocalImage)
    fun updateImage(localImage: LocalImage)
    fun deleteImage(imgId:String)
    fun getListOfImage(placeId:String):List<LocalImage>
}