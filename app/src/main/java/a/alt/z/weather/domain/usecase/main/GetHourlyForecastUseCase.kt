package a.alt.z.weather.domain.usecase.main

import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.HourlyForecast
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetHourlyForecastUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
): UseCase<GetHourlyForecastParameters, List<HourlyForecast>>(coroutineDispatcher) {

    override suspend fun execute(parameters: GetHourlyForecastParameters): List<HourlyForecast> {
        return repository.getHourlyForecast(parameters.baseDate, parameters.baseTime, parameters.x, parameters.y)
    }
}

data class GetHourlyForecastParameters(
    val baseDate: String,
    val baseTime: String,
    val x: Int,
    val y: Int
)