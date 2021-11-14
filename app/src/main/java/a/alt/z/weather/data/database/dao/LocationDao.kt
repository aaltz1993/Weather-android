package a.alt.z.weather.data.database.dao

import a.alt.z.weather.data.database.model.LocationEntity
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: LocationEntity)

    @Update
    suspend fun updateLocation(location: LocationEntity)

    @Query("SELECT * FROM location WHERE isDeviceLocation = :isDeviceLocation")
    suspend fun getDeviceLocation(isDeviceLocation: Boolean = true): LocationEntity?

    @Query("SELECT * FROM location")
    suspend fun getLocationsSnapshot(): List<LocationEntity>

    @Query("SELECT * FROM location")
    fun getLocations(): Flow<List<LocationEntity>>

    @Delete
    suspend fun deleteLocation(location: LocationEntity)
}