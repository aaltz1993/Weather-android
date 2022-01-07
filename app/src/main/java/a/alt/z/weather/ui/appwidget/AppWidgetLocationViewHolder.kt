package a.alt.z.weather.ui.appwidget

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ItemLocationAppWidgetBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView

class AppWidgetLocationViewHolder(
    private val binding: ItemLocationAppWidgetBinding,
    private val onClickAction: ((position: Int) -> Unit)
): RecyclerView.ViewHolder(binding.root) {

    init {
        binding.contentLayout.setOnClickListener {
            binding.locationSelectRadioButton.isChecked = true
        }
        
        binding.locationSelectRadioButton.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                onClickAction(bindingAdapterPosition)
            }
        }
    }
    
    fun bind(location: Location, presentWeather: PresentWeather? = null, isSelected: Boolean) {
        binding.apply {
            locationSelectRadioButton.isChecked = isSelected

            if (locationNameTextView.text == location.name) {
                return
            }

            locationNameTextView.text = location.name

            weatherDescriptionTextView.text = presentWeather?.let { descriptionOf(it.sky, it.precipitationType) }.orEmpty()
            presentWeather
                ?.let { iconResIdOf(it.sky, it.precipitationType) }
                ?.let { ContextCompat.getDrawable(weatherIconImageView.context, it) }
                .let { weatherIconImageView.setImageDrawable(it) }

            temperatureTextView.text = presentWeather?.temperature?.toString().orEmpty()
            temperatureDegree.isVisible = presentWeather != null
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