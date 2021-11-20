package a.alt.z.weather.domain.repository

import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun addLocation(location: Location)

    suspend fun addDeviceLocation(latitude: Double, longitude: Double)

    suspend fun getLocations(): Flow<List<Location>>

    suspend fun deleteLocation(location: Location)

    suspend fun searchAddress(query: String): List<Address>

    suspend fun toggleLocationService(on: Boolean)

    suspend fun getLocationServiceOn(): Flow<Boolean>
}