package a.alt.z.weather.ui.main

import a.alt.z.weather.model.CurrentWeather
import a.alt.z.weather.model.FineParticleGrade
import a.alt.z.weather.model.UltraFineParticleGrade
import a.alt.z.weather.utils.result.Result
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CurrentWeatherViewModel @Inject constructor(): ViewModel() {

    private val _currentWeather = MutableLiveData<Result<CurrentWeather>>()
    val currentWeather: LiveData<Result<CurrentWeather>> = _currentWeather

    fun getCurrentWeather() {
        viewModelScope.launch {
            val stub = CurrentWeather(
                "비",
                29,
                31,
                27,
                20,
                -3,
                FineParticleGrade.GOOD,
                UltraFineParticleGrade.BAD)

            delay(500L)

            _currentWeather.postValue(Result.Success(stub))
        }
    }
}