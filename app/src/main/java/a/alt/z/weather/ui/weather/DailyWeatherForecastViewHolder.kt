package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemDailyForecastBinding
import a.alt.z.weather.model.weather.DailyWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.*

class DailyWeatherForecastViewHolder(
    private val binding: ItemDailyForecastBinding
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(dailyWeather: DailyWeather, isLastItem: Boolean = false) {
        binding.apply {
            when {
                dailyWeather.date == LocalDate.now() -> {
                    weekdayTextView.text = root.context.getString(R.string.today)
                    weekdayTextView.setTextColor(ContextCompat.getColor(root.context, R.color.today_text_color))
                    dateTextView.setTextColor(ContextCompat.getColor(root.context, R.color.today_text_color))
                }
                dailyWeather.date.dayOfWeek == DayOfWeek.SATURDAY || dailyWeather.date.dayOfWeek == DayOfWeek.SUNDAY -> {
                    weekdayTextView.text = dailyWeather.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
                    weekdayTextView.setTextColor(ContextCompat.getColor(root.context, R.color.holiday_text_color))
                    dateTextView.setTextColor(ContextCompat.getColor(root.context, R.color.holiday_text_color))
                }
                else -> {
                    weekdayTextView.text = dailyWeather.date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.KOREA)
                    weekdayTextView.setTextColor(ContextCompat.getColor(root.context, R.color.weekday_text_color))
                    dateTextView.setTextColor(ContextCompat.getColor(root.context, R.color.weekday_text_color))
                }
            }

            dateTextView.text = "${dailyWeather.date.monthValue}/${dailyWeather.date.dayOfMonth}"

            val resIdBeforeNoon = iconResIdOf(dailyWeather.skyBeforeNoon, dailyWeather.precipitationBeforeNoon)
            beforeNoonIconImageView.setImageResource(resIdBeforeNoon)

            val resIdAfternoon = iconResIdOf(dailyWeather.skyAfternoon, dailyWeather.precipitationAfternoon)
            afternoonIconImageView.setImageResource(resIdAfternoon)

            beforeNoonPopTextView.text = "${dailyWeather.probabilityOfPrecipitationBeforeNoon}%"
            afternoonPopTextView.text = "${dailyWeather.probabilityOfPrecipitationAfternoon}%"

            minTemperatureTextView.text = "${dailyWeather.minTemperature}°"
            maxTemperatureTextView.text = "${dailyWeather.maxTemperature}°"

            dayDivider.isVisible = !isLastItem
        }
    }

    private fun iconResIdOf(sky: Sky, precipitation: Precipitation): Int {
        return when (precipitation) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_clear }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy }
                    Sky.OVERCAST -> { R.drawable.icon_overcast }
                }
            }
            Precipitation.RAIN -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_rain }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy_rain }
                    Sky.OVERCAST -> { R.drawable.icon_overcast_rain }
                }
            }
            Precipitation.SNOW -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_snow }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy_snow }
                    Sky.OVERCAST -> { R.drawable.icon_overcast_snow }
                }
            }
            Precipitation.RAIN_SNOW -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_overcast_rain_snow }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy_rain_snow }
                    Sky.OVERCAST -> { R.drawable.icon_overcast_rain_snow }
                }
            }
            Precipitation.SHOWER -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_overcast_shower }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy_shower }
                    Sky.OVERCAST -> { R.drawable.icon_overcast_shower }
                }
            }
        }
    }
}

/*
맑음
구름많음, 구름많고 비, 구름많고 눈, 구름많고 비/눈, 구름많고 소나기
흐림, 흐리고 비, 흐리고 눈, 흐리고 비/눈, 흐리고 소나기
 */