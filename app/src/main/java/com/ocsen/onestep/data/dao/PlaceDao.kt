package com.ocsen.onestep.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entities.LocalPlace

@Dao
abstract class PlaceDao {
    @Query("SELECT * FROM LocalPlaceTable")
    abstract fun getListLocalPlace(): List<LocalPlace>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insertLocalPlace(place: LocalPlace)

    @Query("DELETE FROM LocalPlaceTable WHERE id=:id")
    abstract fun deletePlace(id: String)

}