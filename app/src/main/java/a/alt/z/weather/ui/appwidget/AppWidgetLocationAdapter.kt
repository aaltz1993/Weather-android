package a.alt.z.weather.ui.appwidget

import a.alt.z.weather.databinding.ItemLocationAppWidgetBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class AppWidgetLocationAdapter(
    private val locations: List<Location>,
    private val weathersByLocation: Map<Long, PresentWeather?>
): RecyclerView.Adapter<AppWidgetLocationViewHolder>() {

    private var selectedPosition = 0
        set(value) {
            if (field == value) return

            val old = field
            field = value
            notifyItemChanged(old)
        }

    fun getSelectedLocation(): Location {
        return locations[selectedPosition]
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppWidgetLocationViewHolder {
        return ItemLocationAppWidgetBinding
            .inflate(parent.layoutInflater, parent, false)
            .let { AppWidgetLocationViewHolder(it) { position -> selectedPosition = position } }
    }

    override fun onBindViewHolder(holder: AppWidgetLocationViewHolder, position: Int) {
        val location = locations[position]
        val presentWeather = weathersByLocation[location.id]
        holder.bind(location, presentWeather, position == selectedPosition)
    }

    override fun getItemCount(): Int = weathersByLocation.size
}