package a.alt.z.weather.ui.main

import a.alt.z.weather.databinding.ItemHourlyForecastBinding
import a.alt.z.weather.model.HourlyForecast
import a.alt.z.weather.utils.extensions.dpToPx
import android.annotation.SuppressLint
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.core.view.updateLayoutParams
import androidx.recyclerview.widget.RecyclerView

class HourlyForecastViewHolder(
    private val binding: ItemHourlyForecastBinding
): RecyclerView.ViewHolder(binding.root) {

    private val maxHeight = 64
    private val minHeight = 24

    @SuppressLint("SetTextI18n")
    fun bind(hourlyForecast: HourlyForecast, heightMultiplier: Float) {
        binding.apply {
            temperatureTextView.text = "${hourlyForecast.temperature}°"

            temperatureBar.updateLayoutParams {
                val newHeight = ((maxHeight - minHeight) * heightMultiplier).toInt() + minHeight

                height = binding.root.context.dpToPx(newHeight).toInt()
            }

            if (hourlyForecast.probabilityOfPrecipitation == 0) {
                popTextView.isInvisible = true
            } else {
                popTextView.isVisible = true
                popTextView.text = "${hourlyForecast.probabilityOfPrecipitation}%"
            }

            hourTextView.text = "${hourlyForecast.time.toInt() / 100}시"
        }
    }
}