package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Coordinate
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddDeviceLocationUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocationRepository
): UseCase<Coordinate, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Coordinate) {
        repository.addDeviceLocation(parameters.latitude, parameters.longitude)
    }
}