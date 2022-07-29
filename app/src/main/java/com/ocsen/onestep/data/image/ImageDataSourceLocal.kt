package com.ocsen.onestep.data.image

import android.util.Log
import com.example.data.local.entities.LocalImage
import com.ocsen.onestep.data.LocalDatabase
import java.lang.Exception
import javax.inject.Inject

class ImageDataSourceLocal @Inject constructor(val database: LocalDatabase) : ImageDataSource {
    override fun insertImage(localImage: LocalImage) {
        try {
            database.localImageDao().insertImage(localImage)

        } catch (e: Exception) {
            Log.d("INSERT_IMG", "${e.toString()}")
        }

    }

    override fun updateImage(localImage: LocalImage) {
        database.localImageDao().updateImage(localImage)
    }

    override fun deleteImage(imgId: String) {
        database.localImageDao().deleteImage(imgId)
    }

    override fun getListOfImage(placeId: String): List<LocalImage> {
        return database.localImageDao().getImagesOnPlace(placeId)
    }
}