package a.alt.z.weather.ui.main

import a.alt.z.weather.databinding.ItemHourlyForecastBinding
import a.alt.z.weather.model.HourlyForecast
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import timber.log.debug

class HourlyForecastAdapter(
    private val hourlyForecasts: List<HourlyForecast>
): RecyclerView.Adapter<HourlyForecastViewHolder>() {

    private val maxTemperature = hourlyForecasts.maxOf { it.temperature }

    private val minTemperature = hourlyForecasts.minOf { it.temperature }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HourlyForecastViewHolder {
        return ItemHourlyForecastBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
            .let { HourlyForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: HourlyForecastViewHolder, position: Int) {
        val hourlyForecast = hourlyForecasts[position]

        val multiplier = (hourlyForecast.temperature - minTemperature).toFloat() / (maxTemperature - minTemperature)

        holder.bind(hourlyForecast, multiplier)
    }

    override fun getItemCount(): Int {
        return hourlyForecasts.size
    }
}