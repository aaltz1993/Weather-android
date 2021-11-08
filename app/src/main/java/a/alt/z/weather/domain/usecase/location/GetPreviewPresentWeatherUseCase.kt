package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.PreviewPresentWeather
import a.alt.z.weather.model.weather.PresentWeather
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetPreviewPresentWeatherUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: WeatherRepository
): UseCase<Address, PreviewPresentWeather>(coroutineDispatcher) {

    override suspend fun execute(parameters: Address): PreviewPresentWeather {
        return repository.getPreviewPresentWeather(parameters)
    }
}