package com.ocsen.onestep.data.repositories

import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.image.ImageDataSource
import com.ocsen.onestep.data.place.PlaceDataSource
import javax.inject.Inject

class PlaceAndImageRepositoryImpl @Inject constructor(
    private val placeDataSource: PlaceDataSource,
    private val imageDataSource: ImageDataSource
) : PlaceAndImageRepository {
    override fun insertPlace(place: LocalPlace) {
        placeDataSource.insertPlace(place)
    }

    override fun updatePlace(place: LocalPlace) {
        placeDataSource.updatePlace(place)
    }

    override fun deletePlace(id: String) {
        placeDataSource.deletePlace(id)
    }

    override fun getListOfPlace(): List<LocalPlace> {
        return placeDataSource.getListOfPlace()
    }

    override fun insertImageIdToPlace(placeId: String, imgId: String) {
        // imageDataSource.insertImage()
    }

    override fun updateImageIdToPlace() {

    }

    override fun deleteImageIdOnPlace() {
        TODO("Not yet implemented")
    }

    override fun getListOfImageIdOnPlace() {
        TODO("Not yet implemented")
    }

    override fun insertImage(localImage: LocalImage) {
        imageDataSource.insertImage(localImage = localImage)
    }

    override fun updateImage(localImage: LocalImage) {
        imageDataSource.updateImage(localImage)
    }

    override fun deleteImage(imgId: String) {
        imageDataSource.deleteImage(imgId)
    }

    override fun getListOfImage(placeId: String): List<LocalImage> {
        return imageDataSource.getListOfImage(placeId)
    }


}