package com.ocsen.onestep.di

import android.app.Application
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.location.LocationServices
import com.ocsen.onestep.data.LocalDatabase
import com.ocsen.onestep.data.image.ImageDataSource
import com.ocsen.onestep.data.image.ImageDataSourceLocal
import com.ocsen.onestep.data.place.PlaceDataSource
import com.ocsen.onestep.data.place.PlaceDataSourceLocal
import com.ocsen.onestep.data.repositories.PlaceAndImageRepository
import com.ocsen.onestep.data.repositories.PlaceAndImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideGoogleApiAvailability() = GoogleApiAvailability.getInstance()

    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        application: Application
    ) = LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun provideDataStore(application: Application): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create {
            application.preferencesDataStoreFile("prefs")
        }
    }

    @Provides
    fun provideDb(application: Application): LocalDatabase =
        Room.databaseBuilder(
            application,
            LocalDatabase::class.java,
            "place_info.db"
        ).build()

    @Provides
    fun provideImageDataSource(localDatabase: LocalDatabase): ImageDataSource =
        ImageDataSourceLocal(localDatabase)

    @Provides
    fun providePlaceDataSource(localDatabase: LocalDatabase): PlaceDataSource =
        PlaceDataSourceLocal(localDatabase)

    @Provides
    fun provideRepository(
        placeDataSource: PlaceDataSource,
        imageDataSource: ImageDataSource
    ): PlaceAndImageRepository = PlaceAndImageRepositoryImpl(
        placeDataSource = placeDataSource,
        imageDataSource = imageDataSource
    )

}