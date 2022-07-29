package com.ocsen.onestep.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.dao.ImageDao
import com.ocsen.onestep.data.dao.PlaceDao

@Database(entities = [LocalPlace::class, LocalImage::class], version = 1)
abstract class LocalDatabase : RoomDatabase(){
    abstract fun localPlaceDao(): PlaceDao
    abstract fun localImageDao(): ImageDao
}