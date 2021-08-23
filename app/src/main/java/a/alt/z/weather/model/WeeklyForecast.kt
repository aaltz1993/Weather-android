package a.alt.z.weather.model

data class WeeklyForecast(
    val date: String,

    val maxTemperature: Int,
    val minTemperature: Int,
    val state: String
)