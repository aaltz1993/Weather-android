package a.alt.z.weather.model.weather

import a.alt.z.weather.model.airquality.FineParticleGrade
import a.alt.z.weather.model.airquality.UltraFineParticleGrade
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky

data class PresentWeather(
    val sky: Sky,
    val temperature: Int,
    val precipitationType: PrecipitationType,
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