package a.alt.z.weather.ui.location

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemLocationBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
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

            weatherDescriptionTextView.text = weather?.let { descriptionOf(it.sky, it.precipitationType) }.orEmpty()
            weather
                ?.let { iconResIdOf(it.sky, it.precipitationType) }
                ?.let { ContextCompat.getDrawable(weatherIconImageView.context, it) }
                .let { weatherIconImageView.setImageDrawable(it) }

            temperatureTextView.text = weather?.temperature?.toString().orEmpty()
            temperatureDegree.isVisible = weather != null

            deleteButton.isVisible = isEditing
            deleteButton.setOnClickListener { onDeleteClickAction(location) }
        }
    }

    private fun descriptionOf(sky: Sky, precipitationTypeType: PrecipitationType): String {
        return when (precipitationTypeType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> "맑음"
                    Sky.CLOUDY -> "구름 많음"
                    Sky.OVERCAST -> "흐림"
                }
            }
            PrecipitationType.RAIN -> "비"
            PrecipitationType.SNOW -> "눈"
            PrecipitationType.RAIN_SNOW -> "비, 눈"
            PrecipitationType.SHOWER -> "소나기"
        }
    }

    private fun iconResIdOf(sky: Sky, precipitationTypeType: PrecipitationType): Int {
        return when (precipitationTypeType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_clear_outlined
                    Sky.CLOUDY -> R.drawable.icon_cloudy_outlined
                    Sky.OVERCAST -> R.drawable.icon_overcast_outlined
                }
            }
            PrecipitationType.RAIN -> R.drawable.icon_rain_outlined
            PrecipitationType.SNOW -> R.drawable.icon_snow_outlined
            PrecipitationType.RAIN_SNOW -> R.drawable.icon_rain_snow_outlined
            PrecipitationType.SHOWER -> R.drawable.icon_shower_outlined
        }
    }
}