package a.alt.z.weather.model.weather

import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import org.threeten.bp.LocalDate

data class DailyWeather(
    val date: LocalDate,
    val minTemperature: Int,
    val maxTemperature: Int,
    val skyBeforeNoon: Sky,
    val skyAfternoon: Sky,
    val precipitationBeforeNoon: Precipitation,
    val precipitationAfternoon: Precipitation,
    val probabilityOfPrecipitationBeforeNoon: Int,
    val probabilityOfPrecipitationAfternoon: Int
)