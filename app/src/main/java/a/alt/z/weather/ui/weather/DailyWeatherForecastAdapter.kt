package a.alt.z.weather.ui.weather

import a.alt.z.weather.databinding.ItemDailyForecastBinding
import a.alt.z.weather.model.weather.DailyWeather
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

class DailyWeatherForecastAdapter : ListAdapter<DailyWeather, DailyWeatherForecastViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyWeatherForecastViewHolder {
        return ItemDailyForecastBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { DailyWeatherForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: DailyWeatherForecastViewHolder, position: Int) {
        holder.bind(getItem(position), position == itemCount - 1)
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<DailyWeather>() {
            override fun areItemsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DailyWeather, newItem: DailyWeather): Boolean {
                return oldItem == newItem
            }
        }
    }
}