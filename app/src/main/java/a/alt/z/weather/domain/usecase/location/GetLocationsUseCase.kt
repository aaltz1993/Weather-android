package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.usecase.FlowUseCase
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocationRepository
): FlowUseCase<Unit, List<Location>>(coroutineDispatcher) {

    override suspend fun execute(parameters: Unit): Flow<List<Location>> {
        return repository.getLocations()
    }
}