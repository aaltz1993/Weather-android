package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.databinding.ItemHourlyForecastDayDividerOnboardingBinding
import a.alt.z.weather.databinding.ItemHourlyForecastOnboardingBinding
import a.alt.z.weather.model.weather.HourlyWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.onboarding.OnboardingHourlyForecastViewHolder.HourlyWeatherForecastDayDivider
import a.alt.z.weather.ui.onboarding.OnboardingHourlyForecastViewHolder.HourlyWeatherForecastViewHolder
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDateTime

class OnboardingHourlyForecastAdapter: RecyclerView.Adapter<OnboardingHourlyForecastViewHolder>() {

    private val maxTemperature = 15
    private val minTemperature = 0

    private val hourlyWeathers = listOf(
        HourlyWeather(
            LocalDateTime.now(), 6, Sky.OVERCAST, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(15), 9, Sky.OVERCAST, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(18), 8, Sky.OVERCAST, 60, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(21), 7, Sky.OVERCAST, 60, PrecipitationType.RAIN,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(0), 5, Sky.OVERCAST, 60, PrecipitationType.RAIN,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(3), 5, Sky.OVERCAST, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(6), 6, Sky.CLOUDY, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(9), 9, Sky.CLOUDY, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(12), 11, Sky.CLOUDY, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        ),
        HourlyWeather(
            LocalDateTime.now().withHour(15), 12, Sky.CLEAR, 0, PrecipitationType.NONE,
            Precipitation.NONE, 0, 0F, 0, 0, 0F
        )
    )

    override fun getItemViewType(position: Int): Int {
        return if (position == 4) 1
        else super.getItemViewType(position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingHourlyForecastViewHolder {
        return when (viewType) {
            0 -> {
                ItemHourlyForecastOnboardingBinding
                    .inflate(parent.layoutInflater, parent, false)
                    .let { HourlyWeatherForecastViewHolder(it) }
            }
            1 -> {
                ItemHourlyForecastDayDividerOnboardingBinding
                    .inflate(parent.layoutInflater, parent, false)
                    .let { HourlyWeatherForecastDayDivider(it) }
            }
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: OnboardingHourlyForecastViewHolder, position: Int) {
        if (holder is HourlyWeatherForecastViewHolder) {
            val hourlyWeather = hourlyWeathers[if (position > 4) position - 1 else position]
            val multiplier = (hourlyWeather.temperature - minTemperature).toFloat() / (maxTemperature - minTemperature)
            holder.bind(hourlyWeather, multiplier, position == 0)
        }
    }

    override fun getItemCount(): Int {
        return hourlyWeathers.size + 1
    }
}