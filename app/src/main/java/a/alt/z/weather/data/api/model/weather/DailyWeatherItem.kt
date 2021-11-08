package a.alt.z.weather.data.api.model.weather

import org.threeten.bp.LocalDate

data class DailyWeatherItem(
    val date: LocalDate,
    val popBeforeNoon: Int,
    val popAfternoon: Int,
    val forecastBeforeNoon: String,
    val forecastAfterNoon: String,
    val minTemperature: Int,
    val maxTemperature: Int
)
