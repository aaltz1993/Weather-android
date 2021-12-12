package a.alt.z.weather.domain.usecase.weather

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPresentWeatherBackwardUseCase @Inject constructor(
    @IODispatcher private val coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
): UseCase<Location, PresentWeather>(coroutineDispatcher) {

    override suspend fun execute(parameters: Location): PresentWeather {
        return try {
            repository.getPresentWeatherBackward(parameters)
        } catch (exception: Exception) {
            throw BackwardServerException
        }
    }
}

object BackwardServerException: Exception()