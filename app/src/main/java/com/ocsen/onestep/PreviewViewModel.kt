package com.ocsen.onestep

import android.content.Context
import androidx.camera.core.CameraSelector
import androidx.lifecycle.*
import com.example.data.local.entities.LocalImage
import com.example.data.local.entities.LocalPlace
import com.ocsen.onestep.data.repositories.PlaceAndImageRepository
import com.ocsen.onestep.services.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreviewViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val savedStateHandle: SavedStateHandle,
    val locationRepository: LocationRepository,
    val placeAndImageRepository: PlaceAndImageRepository
) : ViewModel() {
    var lensFacing: Int = CameraSelector.LENS_FACING_BACK

    fun updateCameraLensFacing(lensFacing: Int) {
        this.lensFacing = lensFacing
    }

    val lastLocation = locationRepository.lastLocation


    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun showLoading()
    {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value=true
        }
    }
    fun hideLoading(){
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value=false
        }
    }
//    val placeInfo: MutableStateFlow<PlaceInfo> = MutableStateFlow(PlaceInfo(id = ""))
//    val liveDataPlaceInfo: LiveData<MyResource<PlaceInfo>> = placeInfo.flatMapLatest {
//        if (it.id.isNotBlank()) {
//            usecases.insertPlaceInfo(placeInfo.value) as Flow<MyResource<PlaceInfo>>
//
//        } else
//            flow { }
//
//    }.asLiveData()

    //    fun getPlaceListPlace(): LiveData<MyResource<ArrayList<PlaceInfo>>> {
//        return (usecases.getPlacesInfo() as Flow<MyResource<ArrayList<PlaceInfo>>>).asLiveData()
//    }
//
    fun addImageInfo(localImage: LocalImage) {
        viewModelScope.launch(Dispatchers.IO) {

            placeAndImageRepository.insertImage(localImage)

        }
    }



//    fun getImages(placeId: String): LiveData<MyResource<List<ImageInfo>>> {
//        return (usecases.getImagesInPlace(placeId) as Flow<MyResource<List<ImageInfo>>>).asLiveData()
//    }
//
//
//    fun addPlace(placeInfo: PlaceInfo, latAndLong: Pair<Double, Double>): String {
//
//        placeInfo.lat = latAndLong.first
//        placeInfo.lon = latAndLong.second
//        this.placeInfo.value = placeInfo
//        return placeInfo.id
//    }
//
//    fun updatePlace(id: String, imageInFoList: ArrayList<ImageInfo>) {
//
//    }

}