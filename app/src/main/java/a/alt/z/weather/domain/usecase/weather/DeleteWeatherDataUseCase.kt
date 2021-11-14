package a.alt.z.weather.domain.usecase.weather

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteWeatherDataUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val weatherRepository: WeatherRepository
): UseCase<Long, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Long) {
        TODO("Not yet implemented")
    }
}