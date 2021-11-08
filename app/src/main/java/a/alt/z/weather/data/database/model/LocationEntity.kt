package a.alt.z.weather.data.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "location")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val isDeviceLocation: Boolean,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val region1DepthName: String,
    val region2DepthName: String,
    val region3DepthName: String
)