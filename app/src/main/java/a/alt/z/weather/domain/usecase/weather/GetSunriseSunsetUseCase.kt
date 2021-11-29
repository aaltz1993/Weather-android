package a.alt.z.weather.domain.usecase.weather

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.elements.SunriseSunset
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetSunriseSunsetUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
): UseCase<Location, SunriseSunset>(coroutineDispatcher) {

    override suspend fun execute(parameters: Location): SunriseSunset {
        return repository.getSunriseSunset(parameters)
    }
}