package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemDailyForecastOnboardingBinding
import a.alt.z.weather.model.weather.DailyWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.TextStyle
import java.util.*

class OnboardingDailyForecastViewHolder(
    private val binding: ItemDailyForecastOnboardingBinding
): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(dailyWeather: DailyWeather) {
        binding.apply {
            when {
                dailyWeather.date == LocalDate.now(ZoneId.of("Asia/Seoul")) -> {
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

            val resIdBeforeNoon = iconResIdOf(dailyWeather.skyBeforeNoon, dailyWeather.precipitationTypeBeforeNoon)
            beforeNoonIconImageView.setImageResource(resIdBeforeNoon)

            val resIdAfternoon = iconResIdOf(dailyWeather.skyAfternoon, dailyWeather.precipitationTypeAfternoon)
            afternoonIconImageView.setImageResource(resIdAfternoon)

            beforeNoonPopTextView.text = "${dailyWeather.probabilityOfPrecipitationBeforeNoon - dailyWeather.probabilityOfPrecipitationBeforeNoon % 10}%"
            afternoonPopTextView.text = "${dailyWeather.probabilityOfPrecipitationAfternoon - dailyWeather.probabilityOfPrecipitationAfternoon % 10}%"

            minTemperatureTextView.text = "${dailyWeather.minTemperature}°"
            maxTemperatureTextView.text = "${dailyWeather.maxTemperature}°"
        }
    }

    private fun iconResIdOf(sky: Sky, precipitationType: PrecipitationType): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_clear }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy }
                    Sky.OVERCAST -> { R.drawable.icon_overcast }
                }
            }
            PrecipitationType.RAIN -> {
                when (sky) {
                    Sky.CLEAR -> { R.drawable.icon_rain }
                    Sky.CLOUDY -> { R.drawable.icon_cloudy_rain }
                    Sky.OVERCAST -> { R.drawable.icon_overcast_rain }
                }
            }
            else -> throw IllegalArgumentException()
        }
    }
}