package com.ocsen.onestep.data.image

import com.example.data.local.entities.LocalImage

interface ImageDataSource {
    fun insertImage(localImage: LocalImage)
    fun updateImage(localImage: LocalImage)
    fun deleteImage(imgId:String)
    fun getListOfImage(placeId:String):List<LocalImage>
}