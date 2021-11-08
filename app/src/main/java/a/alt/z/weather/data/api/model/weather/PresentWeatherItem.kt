package a.alt.z.weather.data.api.model.weather

import org.threeten.bp.LocalDateTime

data class PresentWeatherItem(
    val baseDateTime: LocalDateTime,
    val skyCode: Int,
    val temperature: Int,
    val precipitationTypeCode: Int,
    val precipitation: Float,
    val windSpeed: Float,
    val windDirection: Int,
    val humidity: Int,
    val fineParticleValue: Int,
    val ultraFineParticleValue: Int
)
