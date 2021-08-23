package a.alt.z.weather.model

data class HourlyForecast(
    val date: String,
    val time: String,
    val temperature: Int,
    val probabilityOfPrecipitation: Int,
    val sky: Sky,
    val precipitationType: PrecipitationType
)