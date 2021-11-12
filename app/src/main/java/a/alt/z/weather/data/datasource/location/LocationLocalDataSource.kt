package a.alt.z.weather.data.datasource.location

import a.alt.z.weather.data.database.model.LocationEntity
import kotlinx.coroutines.flow.Flow

interface LocationLocalDataSource {

    suspend fun addLocation(location: LocationEntity)

    suspend fun addDeviceLocation(location: LocationEntity)

    suspend fun getLocations(): Flow<List<LocationEntity>>

    suspend fun deleteLocation(location: LocationEntity)

    suspend fun deleteDeviceLocation()

    suspend fun toggleLocationService(on: Boolean)

    suspend fun getLocationServiceOn(): Boolean
}