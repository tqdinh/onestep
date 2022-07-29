package com.ocsen.onestep

import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.sample.foregroundlocation.data.LocationPreferences
import com.ocsen.onestep.services.ForegroundLocationService
import com.ocsen.onestep.services.ForegroundLocationServiceConnection
import com.ocsen.onestep.services.LocationRepository

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val locationPreferences: LocationPreferences,
    private val serviceConnection: ForegroundLocationServiceConnection
) :
    ViewModel(), ServiceConnection by serviceConnection {
    val isReceivingLocationUpdates = locationRepository.isReceivingLocationUpdates
    val lastLocation = locationRepository.lastLocation
    fun toggleLocationUpdates() {
        if (isReceivingLocationUpdates.value) {
            stopLocationUpdates()
        } else {
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        serviceConnection.service?.startLocationUpdates()
        // Store that the user turned on location updates.
        // It's possible that the service was not connected for the above call. In that case, when
        // the service eventually starts, it will check the persisted value and react appropriately.
        viewModelScope.launch {
            locationPreferences.setLocationTurnedOn(true)
        }
    }

    private fun stopLocationUpdates() {
        serviceConnection.service?.stopLocationUpdates()
        // Store that the user turned off location updates.
        // It's possible that the service was not connected for the above call. In that case, when
        // the service eventually starts, it will check the persisted value and react appropriately.
        viewModelScope.launch {
            locationPreferences.setLocationTurnedOn(false)
        }
    }
}