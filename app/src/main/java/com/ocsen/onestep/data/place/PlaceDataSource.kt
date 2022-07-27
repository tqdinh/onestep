package com.ocsen.onestep.data.place

interface PlaceDataSource {
    fun insertPlace()
    fun updatePlace()
    fun deletePlace()
    fun getListOfPlace()

    fun insertImageIdToPlace()
    fun updateImageIdToPlace()
    fun deleteImageIdOnPlace()
    fun getListOfImageIdOnPlace()
}