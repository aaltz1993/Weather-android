package a.alt.z.weather.model.location

data class Address(
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val region1DepthName: String,
    val region2DepthName: String,
    val region3DepthName: String
) {

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

    companion object {
        val INVALID = Address(
            0.0, 0.0,
            "",
            "",
            "",
            ""
        )
    }
}
