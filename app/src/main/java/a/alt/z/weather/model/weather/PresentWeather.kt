package a.alt.z.weather.model.weather

import a.alt.z.weather.R
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
) {

    val description: String get() = when (precipitationType) {
        PrecipitationType.NONE -> {
            when (sky) {
                Sky.CLEAR -> "맑음"
                Sky.CLOUDY -> "구름 많음"
                Sky.OVERCAST -> "흐림"
            }
        }
        PrecipitationType.RAIN -> "비"
        PrecipitationType.SNOW -> "눈"
        PrecipitationType.RAIN_SNOW -> "비, 눈"
        PrecipitationType.SHOWER -> "소나기"
    }

    val iconResourceId: Int get() = when (precipitationType) {
        PrecipitationType.NONE -> {
            when (sky) {
                Sky.CLEAR -> R.drawable.icon_clear_outlined
                Sky.CLOUDY -> R.drawable.icon_cloudy_outlined
                Sky.OVERCAST -> R.drawable.icon_overcast_outlined
            }
        }
        PrecipitationType.RAIN -> R.drawable.icon_rain_outlined
        PrecipitationType.SNOW -> R.drawable.icon_snow_outlined
        PrecipitationType.RAIN_SNOW -> R.drawable.icon_rain_snow_outlined
        PrecipitationType.SHOWER -> R.drawable.icon_shower_outlined
    }

    val imageResourceId: Int get() = when (precipitationType) {
        PrecipitationType.NONE -> {
            when (sky) {
                Sky.CLEAR -> {
                    when {
                        temperature <= 5 -> R.drawable.image_clear_5
                        temperature in 6..9 -> R.drawable.image_clear_6_9
                        temperature in 10..11 -> R.drawable.image_clear_10_11
                        temperature in 12..16 -> R.drawable.image_clear_12_16
                        temperature in 17..19 -> R.drawable.image_clear_17_19
                        temperature in 20..22 -> R.drawable.image_clear_20_22
                        else -> R.drawable.image_clear_20_22
                    }
                }
                Sky.CLOUDY -> {
                    when {
                        temperature <= 5 -> R.drawable.image_cloudy_5
                        temperature in 6..9 -> R.drawable.image_cloudy_6_9
                        temperature in 10..11 -> R.drawable.image_cloudy_10_11
                        temperature in 12..16 -> R.drawable.image_cloudy_12_16
                        temperature in 17..19 -> R.drawable.image_cloudy_17_19
                        temperature in 20..22 -> R.drawable.image_cloudy_20_22
                        else -> R.drawable.image_cloudy_20_22
                    }
                }
                Sky.OVERCAST -> {
                    when {
                        temperature <= 5 -> R.drawable.image_overcast_5
                        temperature in 6..9 -> R.drawable.image_overcast_6_9
                        temperature in 10..11 -> R.drawable.image_overcast_10_11
                        temperature in 12..16 -> R.drawable.image_overcast_12_16
                        temperature in 17..19 -> R.drawable.image_overcast_17_19
                        temperature in 20..22 -> R.drawable.image_overcast_20_22
                        else -> R.drawable.image_overcast_20_22
                    }
                }
            }
        }
        PrecipitationType.RAIN -> {
            when {
                temperature <= 5 -> R.drawable.image_rain_very_cold
                temperature in 6..11 -> R.drawable.image_rain_cold
                temperature in 12..22 -> R.drawable.image_rain_warm
                else -> R.drawable.image_rain_warm
            }
        }
        PrecipitationType.SNOW -> R.drawable.image_snow
        PrecipitationType.RAIN_SNOW -> R.drawable.image_rain_snow
        PrecipitationType.SHOWER -> {
            when {
                temperature <= 5 -> R.drawable.image_shower_very_cold
                temperature in 6..11 -> R.drawable.image_shower_cold
                temperature in 12..22 -> R.drawable.image_shower_warm
                else -> R.drawable.image_shower_warm
            }
        }
    }
}