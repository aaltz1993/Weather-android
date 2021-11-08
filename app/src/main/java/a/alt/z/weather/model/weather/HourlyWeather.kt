package a.alt.z.weather.model.weather

import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import org.threeten.bp.LocalDateTime

data class HourlyWeather(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val sky: Sky,
    val probabilityOfPrecipitation: Int,
    val precipitationType: Precipitation,
    val precipitation: Float,
    val snow: Float,
    val humidity: Int,
    val windDirection: Int,
    val windSpeed: Float
) {

    companion object {
        val DIVIDER = HourlyWeather(
            LocalDateTime.MIN,
            0,
            Sky.CLEAR,
            0,
            Precipitation.NONE,
            0F,
            0F,
            0,
            0,
            0F
        )
    }
}