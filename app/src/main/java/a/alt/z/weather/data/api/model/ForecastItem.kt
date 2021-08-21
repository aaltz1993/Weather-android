package a.alt.z.weather.data.api.model

data class ForecastItems(
        val item: List<ForecastItem>
)

data class ForecastItem(
        val baseDate: String,
        val baseTime: String,
        val fcstDate: String,
        val fcstTime: String,
        val fcstValue: String,
        val category: String,
        val nx: Double,
        val ny: Double

)