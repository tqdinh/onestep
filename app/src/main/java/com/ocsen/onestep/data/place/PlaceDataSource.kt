package com.ocsen.onestep.data.place

import com.example.data.local.entities.LocalPlace

interface PlaceDataSource {
    fun insertPlace(place: LocalPlace)
    fun updatePlace(place:LocalPlace)
    fun deletePlace(id:String)
    fun getListOfPlace():List<LocalPlace>

    fun insertImageIdToPlace(placeId:String, imgId:String)
    fun updateImageIdToPlace()
    fun deleteImageIdOnPlace()
    fun getListOfImageIdOnPlace()
}