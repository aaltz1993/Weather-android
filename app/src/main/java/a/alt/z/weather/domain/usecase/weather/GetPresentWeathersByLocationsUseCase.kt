package a.alt.z.weather.domain.usecase.weather

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.location.PresentWeatherByLocation
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPresentWeathersByLocationsUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
) : UseCase<List<Location>, List<PresentWeatherByLocation>>(coroutineDispatcher) {

    override suspend fun execute(parameters: List<Location>): List<PresentWeatherByLocation> {
        return repository.getPresentWeathersByLocations(parameters)
    }
}