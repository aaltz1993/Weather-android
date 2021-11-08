package a.alt.z.weather.ui.info

import a.alt.z.weather.databinding.ItemWeatherIconsInfoBinding
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WeatherIconsInfoViewHolder(
    private val binding: ItemWeatherIconsInfoBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(icons: List<WeatherIcon>) {
        binding.apply {
            bind(weatherIconImageView1, weatherIconNameTextView1, icons[0])
            bind(weatherIconImageView2, weatherIconNameTextView2, icons[1])
            bind(weatherIconImageView3, weatherIconNameTextView3, icons[2])
            bind(weatherIconImageView4, weatherIconNameTextView4, icons[3])
            bind(weatherIconImageView5, weatherIconNameTextView5, icons[4])
            bind(weatherIconImageView6, weatherIconNameTextView6, icons[5])
            bind(weatherIconImageView7, weatherIconNameTextView7, icons[6])
            bind(weatherIconImageView8, weatherIconNameTextView8, icons[7])
            bind(weatherIconImageView9, weatherIconNameTextView9, icons[8])
            bind(weatherIconImageView10, weatherIconNameTextView10, icons[9])
            bind(weatherIconImageView11, weatherIconNameTextView11, icons[10])
        }
    }

    private fun bind(iconImageView: ImageView, nameTextView: TextView, weatherIcon: WeatherIcon) {
        iconImageView.setImageResource(weatherIcon.drawableResId)
        nameTextView.text = weatherIcon.name
    }
}