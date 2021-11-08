package a.alt.z.weather.domain.usecase.location

import a.alt.z.weather.di.IODispatcher
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.usecase.UseCase
import a.alt.z.weather.model.location.Address
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class SearchAddressUseCase @Inject constructor(
    @IODispatcher coroutineDispatcher: CoroutineDispatcher,
    private val repository: LocationRepository
): UseCase<String, List<Address>>(coroutineDispatcher) {

    override suspend fun execute(parameters: String): List<Address> {
        return repository.searchAddress(parameters)
    }
}