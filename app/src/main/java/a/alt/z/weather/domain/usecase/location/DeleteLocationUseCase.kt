package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : UseCase<Location, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Location) {
        locationRepository.deleteLocation(parameters)
        weatherRepository.deleteWeatherData(parameters.id)
    }
}