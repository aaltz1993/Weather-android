package a.alt.z.weather.model.appwidget

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

data class AppWidgetData(
    val configure: AppWidgetConfigure,
    val isNight: Boolean,
    val locationName: String,
    val temperature: Int,
    val weatherDescription: String,
    @DrawableRes val weatherIconResId: Int,
    val fineParticleGradeName: String,
    @ColorRes val fineParticleGradeTextColor: Int
)