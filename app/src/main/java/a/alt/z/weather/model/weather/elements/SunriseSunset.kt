package a.alt.z.weather.model.weather.elements

import org.threeten.bp.LocalDateTime

data class SunriseSunset(
    val sunrise: LocalDateTime,
    val sunset: LocalDateTime
)
