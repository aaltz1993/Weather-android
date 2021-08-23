package a.alt.z.weather.ui.main

import a.alt.z.weather.domain.usecase.main.GetHourlyForecastParameters
import a.alt.z.weather.domain.usecase.main.GetHourlyForecastUseCase
import a.alt.z.weather.model.HourlyForecast
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForecastWeatherViewModel @Inject constructor(
    private val getHourlyForecastUseCase: GetHourlyForecastUseCase
): ViewModel() {

    private val _hourlyForecast = MutableLiveData<Result<List<HourlyForecast>>>()
    val hourlyForecast: LiveData<Result<List<HourlyForecast>>> = _hourlyForecast

    fun getHourlyForecast(baseDate: String, baseTime: String, x: Int, y: Int) {
        viewModelScope.launch {
            getHourlyForecastUseCase(
                GetHourlyForecastParameters(baseDate, baseTime, x, y),
                _hourlyForecast
            )
        }
    }
}