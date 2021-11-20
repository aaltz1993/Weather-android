package a.alt.z.weather.data.datasource.location

import a.alt.z.weather.data.database.dao.LocationDao
import a.alt.z.weather.data.database.model.LocationEntity
import a.alt.z.weather.data.preferences.PreferencesStorage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LocationLocalDataSourceImpl @Inject constructor(
    private val locationDao: LocationDao,
    private val preferencesStorage: PreferencesStorage
) : LocationLocalDataSource {

    override suspend fun addLocation(location: LocationEntity) {
        locationDao.insertLocation(location)
    }

    override suspend fun addDeviceLocation(location: LocationEntity) {
        val deviceLocation = locationDao.getDeviceLocation()

        if (deviceLocation == null) {
            locationDao.insertLocation(location)
        } else {
            locationDao.updateLocation(
                LocationEntity(
                    deviceLocation.id,
                    true,
                    location.latitude, location.longitude,
                    location.address,
                    location.region1DepthName, location.region2DepthName, location.region3DepthName
                )
            )
        }
    }

    override suspend fun getLocations(): Flow<List<LocationEntity>> {
        return locationDao.getLocations()
    }

    override suspend fun deleteLocation(location: LocationEntity) {
        locationDao.deleteLocation(location)
    }

    override suspend fun deleteDeviceLocation() {
        locationDao
            .getDeviceLocation()
            ?.let { locationDao.deleteLocation(it) }
    }

    override suspend fun setLocationServiceOn(locationServiceOn: Boolean) {
        preferencesStorage.setLocationServiceOn(locationServiceOn)
    }

    override suspend fun getLocationServiceOn(): Flow<Boolean> {
        return preferencesStorage.locationServiceOn
    }
}