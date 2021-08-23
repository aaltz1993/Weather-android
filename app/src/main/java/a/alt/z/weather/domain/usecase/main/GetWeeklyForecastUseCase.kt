package a.alt.z.weather.domain.usecase.main

import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.WeeklyForecast
import kotlinx.coroutines.CoroutineDispatcher

class GetWeeklyForecastUseCase(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
): UseCase<GetWeeklyForecastParameters, List<WeeklyForecast>>(coroutineDispatcher) {

    override suspend fun execute(parameters: GetWeeklyForecastParameters): List<WeeklyForecast> {
        TODO("Not yet implemented")
    }
}

data class GetWeeklyForecastParameters(
    val baseDate: String,
    val baseTime: String,
    val x: Int,
    val y: Int
)