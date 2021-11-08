package a.alt.z.weather.domain.usecase.weather

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPresentWeatherUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
) : UseCase<Location, PresentWeather>(coroutineDispatcher) {

    override suspend fun execute(parameters: Location): PresentWeather {
        return repository.getPresentWeather(parameters)
    }
}