package a.alt.z.weather.data.api.model

data class CurrentWeatherBody(
    val dataType: String,
    val items: CurrentWeatherItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)
