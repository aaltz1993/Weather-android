package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemLocationBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import android.annotation.SuppressLint
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class LocationViewHolder(
    private val binding: ItemLocationBinding,
    private val onDeleteClickAction: ((Location) -> Unit)
): RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(location: Location, weather: PresentWeather? = null, isEditing: Boolean = false) {
        binding.apply {
            locationNameTextView.text = location.name

            presentLocationIconImageView.isVisible = location.isDeviceLocation

            if (weather != null) {
                weatherDescriptionTextView.text = descriptionOf(weather.sky, weather.precipitationType)
                weatherIconImageView.setImageResource(iconResIdOf(weather.sky, weather.precipitationType))

                temperatureTextView.text = weather.temperature.toString()
            }
            temperatureDegree.isVisible = weather != null

            deleteButton.isVisible = isEditing
            deleteButton.setOnClickListener { onDeleteClickAction(location) }
        }
    }

    private fun descriptionOf(sky: Sky, precipitationType: Precipitation): String {
        return when (precipitationType) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> "맑음"
                    Sky.CLOUDY -> "구름 많음"
                    Sky.OVERCAST -> "흐림"
                }
            }
            Precipitation.RAIN -> "비"
            Precipitation.SNOW -> "눈"
            Precipitation.RAIN_SNOW -> "비, 눈"
            Precipitation.SHOWER -> "소나기"
        }
    }

    private fun iconResIdOf(sky: Sky, precipitationType: Precipitation): Int {
        return when (precipitationType) {
            Precipitation.NONE -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_clear_outlined
                    Sky.CLOUDY -> R.drawable.icon_cloudy_outlined
                    Sky.OVERCAST -> R.drawable.icon_overcast_outlined
                }
            }
            Precipitation.RAIN -> R.drawable.icon_rain_outlined
            Precipitation.SNOW -> R.drawable.icon_snow_outlined
            Precipitation.RAIN_SNOW -> R.drawable.icon_rain_snow_outlined
            Precipitation.SHOWER -> R.drawable.icon_shower_outlined
        }
    }
}