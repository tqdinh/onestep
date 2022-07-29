package com.ocsen.onestep.ui.dashboard

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.repositories.PlaceAndImageRepository
import com.ocsen.onestep.services.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    val locationRepository: LocationRepository,
    val placeAndImageRepository: PlaceAndImageRepository
) : ViewModel() {



    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    val lastLocation = locationRepository.lastLocation

    private val _listPlaces = MutableStateFlow<List<LocalPlace>>(listOf())
    val listPlaces = _listPlaces.asStateFlow()

    private val _listImage = MutableStateFlow<List<LocalImage>>(listOf())
    val listImage = _listImage.asStateFlow()


    fun createPlace(localPlace: LocalPlace) {
        viewModelScope.launch(Dispatchers.IO) {
            placeAndImageRepository.insertPlace(localPlace)
        }

    }


    fun getLatitude(): Double? = lastLocation.value?.latitude
    fun getLongitude(): Double? = lastLocation.value?.longitude


    fun getPlaces_(): Flow<List<LocalPlace>> = flow {
        viewModelScope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.IO).launch {
                _listPlaces.value = placeAndImageRepository.getListOfPlace()


            }

        }

    }

    fun getPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.IO).launch {
                _listPlaces.value = placeAndImageRepository.getListOfPlace()
            }

        }
    }

    fun getListOfImage(placeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.IO).launch {
                _listImage.value = placeAndImageRepository.getListOfImage(placeId)
            }

        }

    }
}