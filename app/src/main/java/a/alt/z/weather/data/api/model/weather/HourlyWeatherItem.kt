package a.alt.z.weather.data.api.model.weather

import org.threeten.bp.LocalDateTime

data class HourlyWeatherItem(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val skyCode: Int,
    val precipitationCode: Int,
    val precipitation: String,
    val probabilityOfPrecipitation: Int,
    val snow: String,
    val minTemperature: Int,
    val maxTemperature: Int,
    val humidity: Int,
    val windDirection: Int,
    val windSpeed: Float
)
