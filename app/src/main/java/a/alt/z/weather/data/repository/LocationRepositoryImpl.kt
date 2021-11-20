package a.alt.z.weather.data.repository

import a.alt.z.weather.data.api.model.address.AddressItem
import a.alt.z.weather.data.database.model.LocationEntity
import a.alt.z.weather.data.datasource.location.LocationLocalDataSource
import a.alt.z.weather.data.datasource.location.LocationRemoteDataSource
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val localDataSource: LocationLocalDataSource,
    private val remoteDataSource: LocationRemoteDataSource
): LocationRepository {

    override suspend fun addLocation(location: Location) {
        localDataSource.addLocation(location.transform())
    }

    private fun Location.transform() = LocationEntity(
        id,
        isDeviceLocation,
        latitude, longitude,
        address,
        region1DepthName, region2DepthName, region3DepthName
    )

    override suspend fun addDeviceLocation(latitude: Double, longitude: Double) {
        remoteDataSource.coordinateToAddress(latitude, longitude)
            .transform(true)
            .let { localDataSource.addDeviceLocation(it) }
    }

    private fun AddressItem.transform(isDeviceLocation: Boolean) = LocationEntity(
        0L,
        isDeviceLocation,
        latitude, longitude,
        address,
        region1depthName, region2depthName, region3depthName
    )

    override suspend fun getLocations(): Flow<List<Location>> {
        return localDataSource.getLocations()
            .map { locations -> locations.map { it.transform() } }
            .distinctUntilChanged()
    }

    private fun LocationEntity.transform() = Location(
        id,
        isDeviceLocation,
        latitude, longitude,
        address,
        region1DepthName, region2DepthName, region3DepthName
    )

    override suspend fun deleteLocation(location: Location) {
        localDataSource.deleteLocation(location.transform())
    }

    override suspend fun deleteDeviceLocation() {
        localDataSource.deleteDeviceLocation()
    }

    override suspend fun searchAddress(query: String): List<Address> {
        return remoteDataSource.searchAddress(query).map {
            Address(
                it.latitude, it.longitude,
                it.address,
                it.region1depthName, it.region2depthName, it.region3depthName
            )
        }
    }

    override suspend fun toggleLocationService(on: Boolean) {
        localDataSource.setLocationServiceOn(on)
    }

    override suspend fun getLocationServiceOn(): Flow<Boolean> {
        return localDataSource.getLocationServiceOn()
    }
}