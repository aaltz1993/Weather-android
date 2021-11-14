package a.alt.z.weather.ui.dialog

import a.alt.z.weather.databinding.ItemWeatherIconsInfoBinding
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class WeatherIconsInfoAdapter(
    private val weatherIcons: List<List<WeatherIcon>>
) : RecyclerView.Adapter<WeatherIconsInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherIconsInfoViewHolder {
        return ItemWeatherIconsInfoBinding
            .inflate(parent.layoutInflater, parent, false)
            .let { WeatherIconsInfoViewHolder(it) }
    }

    override fun onBindViewHolder(holder: WeatherIconsInfoViewHolder, position: Int) {
        holder.bind(weatherIcons[position])
    }

    override fun getItemCount(): Int {
        return weatherIcons.size
    }
}