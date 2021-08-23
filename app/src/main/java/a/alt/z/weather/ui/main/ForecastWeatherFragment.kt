package a.alt.z.weather.ui.main

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentForecastWeatherBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

@AndroidEntryPoint
class ForecastWeatherFragment : BaseFragment(R.layout.fragment_forecast_weather) {

    private val binding by viewBinding(FragmentForecastWeatherBinding::bind)

    private val viewModel: ForecastWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val now = LocalDateTime.now()

        val base = if (now.hour > 2) {
            now
        } else {
            now.minusDays(1)
        }

        val baseDate = DateTimeFormatter.ofPattern("yyyyMMdd").format(base)
        val baseTime = "0200"

        viewModel.getHourlyForecast(baseDate, baseTime, 60, 127) /* Stub: 서울 */
    }

    override fun initView() {
        binding.hourlyForecastRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    override fun setupObserver() {
        viewModel.hourlyForecast.observe(viewLifecycleOwner) { result ->
            result
                .successOrNull()
                ?.let { binding.hourlyForecastRecyclerView.adapter = HourlyForecastAdapter(it) }
        }
    }
}