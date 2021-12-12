package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemHourlyForecastBinding
import a.alt.z.weather.databinding.ItemHourlyForecastDayDividerBinding
import a.alt.z.weather.model.weather.HourlyWeather
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import org.threeten.bp.LocalDate
import timber.log.Timber
import timber.log.debug

class HourlyWeatherForecastAdapter: ListAdapter<HourlyWeather, HourlyWeatherForecastViewHolders>(diffCallback) {

    private val maxTemperature by lazy { currentList.maxOfOrNull { it.temperature } ?: 0 }

    private val minTemperature: Int by lazy {
        currentList
            .filterNot { it.temperature == Int.MIN_VALUE }
            .minOfOrNull { it.temperature } ?: 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) == HourlyWeather.DIVIDER) return 0 else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyWeatherForecastViewHolders {
        return when (viewType) {
            0 -> {
                ItemHourlyForecastDayDividerBinding
                    .inflate(parent.layoutInflater, parent, false)
                    .let { HourlyWeatherForecastViewHolders.HourlyWeatherForecastDayDivider(it) }
            }
            1 -> {
                ItemHourlyForecastBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
                    .let { HourlyWeatherForecastViewHolders.HourlyWeatherForecastViewHolder(it) }
            }
            else -> throw IllegalStateException()
        }
    }

    override fun onBindViewHolder(holder: HourlyWeatherForecastViewHolders, position: Int) {
        when (holder) {
            is HourlyWeatherForecastViewHolders.HourlyWeatherForecastViewHolder -> {
                val hourlyForecast = getItem(position)
                val multiplier = (hourlyForecast.temperature - minTemperature).toFloat() / (maxTemperature - minTemperature)
                holder.bind(hourlyForecast, multiplier, position == 0)
            }
            is HourlyWeatherForecastViewHolders.HourlyWeatherForecastDayDivider -> {
                if (getItem(position - 1).dateTime.toLocalDate() == LocalDate.now()) {
                    holder.bind(R.string.tomorrow)
                } else {
                    holder.bind(R.string.day_after_tomorrow)
                }
            }
        }
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<HourlyWeather>() {
            override fun areItemsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: HourlyWeather, newItem: HourlyWeather): Boolean {
                return oldItem == newItem
            }
        }
    }
}