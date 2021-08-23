package a.alt.z.weather.ui.main

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentCurrentWeatherBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDate
import org.threeten.bp.format.TextStyle
import java.util.*
import kotlin.math.abs

@AndroidEntryPoint
class CurrentWeatherFragment : BaseFragment(R.layout.fragment_current_weather) {

    private val binding by viewBinding(FragmentCurrentWeatherBinding::bind)

    private val viewModel: CurrentWeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getCurrentWeather()
    }


    override fun initView() {
        val today = LocalDate.now()

        binding.apply {
            @SuppressLint("SetTextI18n")
            dateTextView.text = "${today.monthValue}월 ${today.dayOfMonth}일 ${today.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.KOREA)}"

            addressTextView.text = "서울 합정동"
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        viewModel.currentWeather.observe(viewLifecycleOwner) { result ->
            result
                .successOrNull()
                ?.let { currentWeather ->
                    binding.apply {
                        descriptionTextView.text = "은 ${currentWeather.description}"
                        currentTemperatureTextView.text = "${currentWeather.temperature}°"
                        compareYesterdayTextView.text = "어제보다 ${abs(currentWeather.compareYesterday)}° ↓"
                        minTemperatureTextView.text = "${currentWeather.minTemperature}°"
                        maxTemperatureTextView.text = "${currentWeather.maxTemperature}°"
                    }
                }
        }
    }
}