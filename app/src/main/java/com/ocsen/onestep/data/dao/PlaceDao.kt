package com.ocsen.onestep.data.dao

import androidx.room.*
import com.example.data.local.entities.LocalPlace

@Dao
abstract class PlaceDao {
    @Query("SELECT * FROM LocalPlaceTable")
    abstract fun getListLocalPlace(): List<LocalPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocalPlace(place: LocalPlace)

    @Query("DELETE FROM LocalPlaceTable WHERE id=:id")
    abstract fun deletePlace(id: String)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    abstract fun updatePlace(place: LocalPlace)

}