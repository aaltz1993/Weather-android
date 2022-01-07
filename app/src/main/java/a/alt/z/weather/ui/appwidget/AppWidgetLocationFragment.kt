package a.alt.z.weather.ui.appwidget

import a.alt.z.weather.R
import a.alt.z.weather.appwidget.AppWidgetDataManager
import a.alt.z.weather.databinding.FragmentAppWidgetLocationBinding
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.showToast
import a.alt.z.weather.utils.extensions.viewBinding
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class AppWidgetLocationFragment : Fragment(R.layout.fragment_app_widget_location) {

    private val binding by viewBinding(FragmentAppWidgetLocationBinding::bind)

    private val appWidgetDataManager: AppWidgetDataManager by lazy {
        AppWidgetDataManager.getInstance(requireContext())
    }

    private val weathersByLocation = mutableMapOf<Long, PresentWeather?>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            val locations = withContext(Dispatchers.IO) {
                appWidgetDataManager.getLocations()
            }

            locations.forEach { location ->
                val presentWeather = withContext(Dispatchers.IO) {
                    try {
                        appWidgetDataManager.getPresentWeather(location)
                    } catch (exception: Exception) {
                        Timber.d(exception)
                        null
                    }
                }

                weathersByLocation[location.id] = presentWeather
            }

            val adapter = AppWidgetLocationAdapter(locations, weathersByLocation)

            binding.apply {
                backButton.setOnClickListener {
                    val location = adapter.getSelectedLocation()
                    val presentWeather = weathersByLocation[location.id]

                    if (presentWeather == null) {
                        showToast { "Something goes wrong X(" }
                        return@setOnClickListener
                    }

                    parentFragmentManager.setFragmentResult(
                        RequestKeys.SELECTED_LOCATION_WEATHER,
                        bundleOf(
                            Pair(ResultKeys.APP_WIDGET_LOCATION, location),
                            Pair(ResultKeys.APP_WIDGET_WEATHER, presentWeather)
                        )
                    )

                    parentFragmentManager.commit { remove(this@AppWidgetLocationFragment) }
                }

                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())

                loadingIndicatorView.isVisible = false
            }
        }
    }


}