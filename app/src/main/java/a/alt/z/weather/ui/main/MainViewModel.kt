package a.alt.z.weather.ui.main

import a.alt.z.weather.domain.usecase.invoke
import a.alt.z.weather.domain.usecase.location.AddDeviceLocationUseCase
import a.alt.z.weather.domain.usecase.location.GetLocationServiceOnUseCase
import a.alt.z.weather.domain.usecase.location.GetLocationsUseCase
import a.alt.z.weather.domain.usecase.others.GetSkipOnboardingUseCase
import a.alt.z.weather.domain.usecase.others.SetSkipOnboardingUseCase
import a.alt.z.weather.domain.usecase.weather.GetSunriseSunsetUseCase
import a.alt.z.weather.model.location.Coordinate
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.elements.SunriseSunset
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSkipOnboardingUseCase: GetSkipOnboardingUseCase,
    private val addDeviceLocationUseCase: AddDeviceLocationUseCase,
    private val getLocationsUseCase: GetLocationsUseCase,
    private val getSunriseSunsetUseCase: GetSunriseSunsetUseCase
) : ViewModel() {

    private val _locations = MutableLiveData<Result<List<Location>>>()
    val locations: LiveData<Result<List<Location>>> = _locations

    private val _skipOnboarding = MutableLiveData<Result<Boolean>>()
    val skipOnboarding: LiveData<Result<Boolean>> = _skipOnboarding

    private val _sunriseSunset = MutableLiveData<Result<SunriseSunset>>()
    val sunriseSunset: LiveData<Result<SunriseSunset>> = _sunriseSunset

    init {
        viewModelScope.launch { getSkipOnboardingUseCase(_skipOnboarding) }
    }

    fun getLocations() {
        viewModelScope.launch { getLocationsUseCase(_locations) }
    }

    fun addDeviceLocation(coordinate: Coordinate) {
        viewModelScope.launch { addDeviceLocationUseCase(coordinate) }
    }

    fun getSunriseSunset(location: Location) {
        viewModelScope.launch { getSunriseSunsetUseCase(location, _sunriseSunset) }
    }
}