package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocationRepository
) : UseCase<Location, Unit>(coroutineDispatcher) {

    override suspend fun execute(parameters: Location) {
        repository.deleteLocation(parameters)
    }
}