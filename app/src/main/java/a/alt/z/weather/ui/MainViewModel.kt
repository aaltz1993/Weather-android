package a.alt.z.weather.ui

import a.alt.z.weather.domain.usecase.invoke
import a.alt.z.weather.domain.usecase.location.AddDeviceLocationUseCase
import a.alt.z.weather.domain.usecase.location.GetLocationsUseCase
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addDeviceLocationUseCase: AddDeviceLocationUseCase,
    private val getLocationsUseCase: GetLocationsUseCase
) : ViewModel() {

    private val _updateCurrentLocationResult = MutableLiveData<Result<Unit>>()
    val updateCurrentLocationResult: LiveData<Result<Unit>> = _updateCurrentLocationResult

    private val _locations = MutableLiveData<Result<List<Location>>>()
    val locations: LiveData<Result<List<Location>>> = _locations

    init {
        viewModelScope.launch {
            getLocationsUseCase(_locations)
        }
    }
}