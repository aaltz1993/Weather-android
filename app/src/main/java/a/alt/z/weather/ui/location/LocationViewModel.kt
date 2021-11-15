package a.alt.z.weather.ui.location

import a.alt.z.weather.domain.usecase.invoke
import a.alt.z.weather.domain.usecase.location.*
import a.alt.z.weather.domain.usecase.weather.GetPresentWeathersByLocationsUseCase
import a.alt.z.weather.model.location.*
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getPresentWeathersByLocationsUseCase: GetPresentWeathersByLocationsUseCase,
    private val searchAddressUseCase: SearchAddressUseCase,
    private val getPreviewPresentWeatherUseCase: GetPreviewPresentWeatherUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val deleteLocationUseCase: DeleteLocationUseCase,
    private val addDeviceLocationUseCase: AddDeviceLocationUseCase,
    private val toggleLocationServiceUseCase: ToggleLocationServiceUseCase,
    private val getLocationServiceOnUseCase: GetLocationServiceOnUseCase,
    private val deleteDeviceLocationUseCase: DeleteDeviceLocationUseCase
) : ViewModel() {

    private val _locations = MutableLiveData<Result<List<Location>>>()
    val locations: LiveData<Result<List<Location>>> = _locations

    private val _searchAddressResult = MutableLiveData<Result<List<Address>>>()
    val searchAddressResult: LiveData<Result<List<Address>>> = _searchAddressResult

    private val _addDeviceLocationResult = MutableLiveData<Result<Unit>>()
    val addDeviceLocationResult: LiveData<Result<Unit>> = _addDeviceLocationResult

    private val _addLocationResult = MutableLiveData<Result<Unit>>()
    val addLocationResult: LiveData<Result<Unit>> = _addLocationResult

    private val _presentWeathersByLocations = MutableLiveData<Result<List<PresentWeatherByLocation>>>()
    val presentWeathersByLocations: LiveData<Result<List<PresentWeatherByLocation>>> = _presentWeathersByLocations

    private val _previewPresentWeather = MutableLiveData<Result<PreviewPresentWeather>>()
    val previewPresentWeather: LiveData<Result<PreviewPresentWeather>> = _previewPresentWeather

    private val _isEditing = MutableLiveData<Boolean>()
    val isEditing: LiveData<Boolean> = _isEditing

    private val _locationServiceOn = MutableLiveData<Result<Boolean>>()
    val locationServiceOn: LiveData<Result<Boolean>> = _locationServiceOn

    init {
        viewModelScope.launch {
            getLocationsUseCase(_locations)
        }
        viewModelScope.launch {
            getLocationServiceOnUseCase(_locationServiceOn)
        }
    }

    fun addDeviceLocation(latitude: Double, longitude: Double) {
        viewModelScope.launch {
            addDeviceLocationUseCase(Coordinate(latitude, longitude), _addDeviceLocationResult)
        }
    }

    fun toggleLocationService(on: Boolean) {
        viewModelScope.launch {
            toggleLocationServiceUseCase(on)
        }
    }

    fun deleteLocation(location: Location) {
        viewModelScope.launch {
            deleteLocationUseCase(location)
        }
    }

    fun deleteDeviceLocation() {
        viewModelScope.launch {
            deleteDeviceLocationUseCase()
        }
    }

    fun searchAddress(query: String) {
        viewModelScope.launch {
            searchAddressUseCase(query, _searchAddressResult)
        }
    }

    fun getPreviewPresentWeather(address: Address) {
        viewModelScope.launch {
            getPreviewPresentWeatherUseCase(address, _previewPresentWeather)
        }
    }

    fun addLocation(address: Address) {
        viewModelScope.launch {
            addLocationUseCase(
                Location(
                    0,
                    false,
                    address.latitude, address.longitude,
                    address.address,
                    address.region1DepthName, address.region2DepthName, address.region3DepthName
                ),
                _addLocationResult
            )
        }
    }

    fun getPresentWeathersByLocations(locations: List<Location>) {
        viewModelScope.launch {
            getPresentWeathersByLocationsUseCase(locations, _presentWeathersByLocations)
        }
    }

    fun toggleEditState() {
        _isEditing.postValue(!(_isEditing.value ?: false))
    }
}