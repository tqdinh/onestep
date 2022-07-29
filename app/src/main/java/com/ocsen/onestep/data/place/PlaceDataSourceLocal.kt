package com.ocsen.onestep.data.place

import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.LocalDatabase
import com.ocsen.onestep.domain.PlaceInfo
import javax.inject.Inject

class PlaceDataSourceLocal @Inject constructor(val database: LocalDatabase) : PlaceDataSource {
    override fun insertPlace(place:LocalPlace) {
        database.localPlaceDao().insertLocalPlace(place)
    }

    override fun updatePlace(place:LocalPlace) {
        database.localPlaceDao().updatePlace(place)
    }

    override fun deletePlace(id:String) {
        database.localPlaceDao().deletePlace(id)
    }

    override fun getListOfPlace():List<LocalPlace> {
        return database.localPlaceDao().getListLocalPlace()
    }

    override fun insertImageIdToPlace(placeId:String, imgId:String) {

    }

    override fun updateImageIdToPlace() {
        TODO("Not yet implemented")
    }

    override fun deleteImageIdOnPlace() {
        TODO("Not yet implemented")
    }

    override fun getListOfImageIdOnPlace() {
        TODO("Not yet implemented")
    }
}