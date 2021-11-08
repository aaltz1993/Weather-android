package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteDeviceLocationUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocationRepository
): UseCase<Unit, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit) {
        repository.deleteDeviceLocation()
    }
}