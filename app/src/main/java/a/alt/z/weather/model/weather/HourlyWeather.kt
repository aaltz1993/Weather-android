package a.alt.z.weather.model.weather

import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.model.weather.elements.Snow
import org.threeten.bp.LocalDateTime

data class HourlyWeather(
    val dateTime: LocalDateTime,
    val temperature: Int,
    val sky: Sky,
    val probabilityOfPrecipitation: Int,
    val precipitationType: PrecipitationType,
    val precipitation: Precipitation,
    val precipitationValue: Int,
    val snow: Snow,
    val snowValue: Float,
    val humidity: Int,
    val windDirection: Int,
    val windSpeed: Float
) {

    companion object {
        val DIVIDER = HourlyWeather(
            LocalDateTime.MIN,
            Int.MIN_VALUE,
            Sky.CLEAR,
            Int.MIN_VALUE,
            PrecipitationType.NONE,
            Precipitation.NONE,
            Int.MIN_VALUE,
            Snow.NONE,
            Float.MIN_VALUE,
            Int.MIN_VALUE,
            Int.MIN_VALUE,
            Float.MIN_VALUE
        )
    }
}