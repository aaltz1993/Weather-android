package a.alt.z.weather.data.api.model.address

data class AddressItem(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val region1depthName: String,
    val region2depthName: String,
    val region3depthName: String
)
