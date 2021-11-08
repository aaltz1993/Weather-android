package a.alt.z.weather.model.location

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val id: Long,
    val isDeviceLocation: Boolean,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val region1DepthName: String,
    val region2DepthName: String,
    val region3DepthName: String
) : Parcelable {

    val name: String get() {
        return when {
            region3DepthName.isNotBlank() -> {
                "${region1DepthName.take(2)} $region3DepthName"
            }
            region2DepthName.isNotBlank() -> {
                "${region1DepthName.take(2)} $region2DepthName"
            }
            else -> region1DepthName
        }
    }
}
