package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemHourlyForecastBinding
import a.alt.z.weather.databinding.ItemHourlyForecastDayDividerBinding
import a.alt.z.weather.model.weather.HourlyWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.utils.extensions.pixelsOf
import android.annotation.SuppressLint
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDateTime

sealed class HourlyWeatherForecastViewHolders(itemView: View): RecyclerView.ViewHolder(itemView) {

    class HourlyWeatherForecastViewHolder(
        private val binding: ItemHourlyForecastBinding
    ): HourlyWeatherForecastViewHolders(binding.root) {

        private val maxHeight = 48
        private val minHeight = 12

        @SuppressLint("SetTextI18n")
        fun bind(hourlyWeather: HourlyWeather, heightMultiplier: Float, isNow: Boolean = false) {
            binding.apply {
                if (isNow) {
                    hourTextView.setTextColor(root.context.getColor(R.color.now_text_color))
                    hourTextView.text = root.context.getString(R.string.now)
                } else {
                    hourTextView.setTextColor(root.context.getColor(R.color.hour_text_color))
                    hourTextView.text = "${hourlyWeather.dateTime.hour}시"
                }

                temperatureTextView.text = "${hourlyWeather.temperature}°"

                temperatureBar.updateLayoutParams {
                    val newHeight = ((maxHeight - minHeight) * heightMultiplier).toInt() + minHeight
                    height = root.context.pixelsOf(newHeight).toInt()
                }

                val resId = iconResIdOf(hourlyWeather.dateTime, hourlyWeather.sky, hourlyWeather.precipitationType)
                iconImageView.setImageResource(resId)

                if (hourlyWeather.probabilityOfPrecipitation == 0) {
                    popTextView.isInvisible = true
                } else {
                    popTextView.isVisible = true
                    popTextView.text = "${hourlyWeather.probabilityOfPrecipitation}%"
                }
            }
        }

        private fun iconResIdOf(dateTime: LocalDateTime, sky: Sky, precipitation: Precipitation): Int {
            val isNight = dateTime.hour >= 18 || dateTime.hour < 6

            return when (precipitation) {
                Precipitation.NONE -> {
                    when (sky) {
                        Sky.CLEAR -> {
                            if (isNight) R.drawable.icon_clear_night
                            else R.drawable.icon_clear
                        }
                        Sky.CLOUDY -> {
                            if (isNight) R.drawable.icon_cloudy_night
                            else R.drawable.icon_cloudy
                        }
                        Sky.OVERCAST -> {
                            R.drawable.icon_overcast
                        }
                    }
                }
                Precipitation.RAIN -> {
                    when (sky) {
                        Sky.CLEAR -> {
                            R.drawable.icon_rain
                        }
                        Sky.CLOUDY -> {
                            if (isNight) R.drawable.icon_cloudy_rain_night
                            else R.drawable.icon_cloudy_rain
                        }
                        Sky.OVERCAST -> {
                            R.drawable.icon_overcast_rain
                        }
                    }
                }
                Precipitation.SNOW -> {
                    when (sky) {
                        Sky.CLEAR -> {
                            R.drawable.icon_snow
                        }
                        Sky.CLOUDY -> {
                            if (isNight) R.drawable.icon_cloudy_snow_night
                            else R.drawable.icon_cloudy_snow
                        }
                        Sky.OVERCAST -> {
                            R.drawable.icon_overcast_snow
                        }
                    }
                }
                Precipitation.RAIN_SNOW -> {
                    when (sky) {
                        Sky.CLEAR -> {
                            R.drawable.icon_overcast_rain_snow
                        }
                        Sky.CLOUDY -> {
                            if (isNight) R.drawable.icon_cloudy_rain_snow_night
                            else R.drawable.icon_cloudy_rain_snow
                        }
                        Sky.OVERCAST -> {
                            R.drawable.icon_overcast_rain_snow
                        }
                    }
                }
                Precipitation.SHOWER -> {
                    when (sky) {
                        Sky.CLEAR -> {
                            R.drawable.icon_overcast_shower
                        }
                        Sky.CLOUDY -> {
                            if (isNight) R.drawable.icon_cloudy_rain_snow_night
                            else R.drawable.icon_cloudy_rain_snow
                        }
                        Sky.OVERCAST -> {
                            R.drawable.icon_overcast_shower
                        }
                    }
                }
            }
        }
    }

    class HourlyWeatherForecastDayDivider(
        private val binding: ItemHourlyForecastDayDividerBinding
    ): HourlyWeatherForecastViewHolders(binding.root) {

        fun bind(dayNameStringResId: Int) {
            binding.dayName.setText(dayNameStringResId)
        }
    }
}

