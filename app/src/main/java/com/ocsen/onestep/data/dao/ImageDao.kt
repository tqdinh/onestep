package com.ocsen.onestep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace

@Dao
abstract class ImageDao {
    @Query("SELECT * FROM LocalImageTable")
    abstract fun getListLocalImage(): Array<LocalImage>

    @Insert
    abstract fun insertImage(localImage: LocalImage)

    @Query("SELECT * FROM LocalImageTable WHERE place_id=:placeId")
    abstract fun getImagesOnPlace(placeId: String): List<LocalImage>

    @Query("DELETE FROM LocalImageTable WHERE id=:imgId")
    abstract fun deleteImage(imgId: String)


}