package a.alt.z.weather.ui.weather

import a.alt.z.weather.domain.usecase.weather.*
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.*
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getPresentWeatherUseCase: GetPresentWeatherUseCase,
    private val getForecastWeatherUseCase: GetForecastWeatherUseCase
) : ViewModel() {

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location> = _location

    private val _presentWeather = MutableLiveData<Result<PresentWeather>>()
    val presentWeather: LiveData<Result<PresentWeather>> = _presentWeather

    private val _forecastWeather = MutableLiveData<Result<ForecastWeather>>()
    val forecastWeather: LiveData<Result<ForecastWeather>> = _forecastWeather

    private val _dataLoaded = MediatorLiveData<Boolean>()
    val dataLoaded: LiveData<Boolean> = _dataLoaded

    init {
        _location.observeForever { location ->
            viewModelScope.launch {
                getPresentWeatherUseCase(location, _presentWeather)
                getForecastWeatherUseCase(location, _forecastWeather)
            }
        }

        _dataLoaded.addSource(_presentWeather) {
            _dataLoaded.postValue(
                _forecastWeather.value is Result.Success
                        && _presentWeather.value is Result.Success
            )
        }

        _dataLoaded.addSource(_forecastWeather) {
            _dataLoaded.postValue(
                _forecastWeather.value is Result.Success
                        && _presentWeather.value is Result.Success
            )
        }
    }

    fun setLocation(location: Location) {
        _location.postValue(location)
    }
}