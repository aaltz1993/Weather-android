package a.alt.z.weather.ui.location

import a.alt.z.weather.databinding.ItemLocationBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.location.PresentWeatherByLocation
import a.alt.z.weather.utils.extensions.layoutInflater
import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

@SuppressLint("NotifyDataSetChanged")
class LocationAdapter(
    private val onDeleteClickAction: ((Location) -> Unit)
): ListAdapter<Location, LocationViewHolder>(diffCallback) {

    var presentWeathersByLocations = listOf<PresentWeatherByLocation>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    var isEditing: Boolean = false
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        return ItemLocationBinding
            .inflate(parent.layoutInflater, parent, false)
            .let { LocationViewHolder(it, onDeleteClickAction) }
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val location = getItem(position)
        val presentWeather = presentWeathersByLocations
            .find { it.location == location }
            ?.weather
        holder.bind(location, presentWeather, isEditing)
    }

    companion object {
        private val diffCallback = object: DiffUtil.ItemCallback<Location>() {
            override fun areItemsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Location, newItem: Location): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}