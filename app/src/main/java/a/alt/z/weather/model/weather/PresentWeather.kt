package a.alt.z.weather.model.weather

import a.alt.z.weather.model.air.FineParticleGrade
import a.alt.z.weather.model.air.UltraFineParticleGrade
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky

data class PresentWeather(
    val sky: Sky,
    val temperature: Int,
    val precipitationType: Precipitation,
    val precipitation: Float,
    val windSpeed: Float,
    val windDirection: Int,
    val humidity: Int,
    /* air quality */
    val fineParticleGrade: FineParticleGrade,
    val fineParticleValue: Int,
    val ultraFineParticleGrade: UltraFineParticleGrade,
    val ultraFineParticleValue: Int,
)