package com.ocsen.onestep.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.repositories.PlaceAndImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @Inject constructor(val placeAndImageRepository: PlaceAndImageRepository): ViewModel() {

    private val _listPlaces = MutableStateFlow<List<LocalPlace>>(listOf())
    val listPlaces = _listPlaces.asStateFlow()

    fun getPlaces() {
        viewModelScope.launch(Dispatchers.IO) {
            CoroutineScope(Dispatchers.IO).launch {
                _listPlaces.value = placeAndImageRepository.getListOfPlace()
            }

        }

    }
}