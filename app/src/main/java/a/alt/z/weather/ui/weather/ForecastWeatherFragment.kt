package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentForecastWeatherBinding
import a.alt.z.weather.model.weather.HourlyWeather
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.dialog.AirQualityIndexDialogFragment
import a.alt.z.weather.ui.dialog.WeatherIconsInfoDialogFragment
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.annotation.SuppressLint
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class ForecastWeatherFragment : BaseFragment(R.layout.fragment_forecast_weather) {

    private val binding by viewBinding(FragmentForecastWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels(ownerProducer = { parentFragment as Fragment })

    private val forecastHourlyWeatherAdapter = HourlyWeatherForecastAdapter()

    private val forecastDailyWeatherAdapter = DailyWeatherForecastAdapter()

    override fun initView() {
        binding.apply {
            scrollView.onPullDown = { dY ->
                parentFragmentManager.setFragmentResult(
                    RequestKeys.ON_ACTION_MOVE,
                    bundleOf(Pair(ResultKeys.ON_ACTION_MOVE, dY))
                )
            }

            scrollView.onActionUp = { dY ->
                parentFragmentManager.setFragmentResult(
                    RequestKeys.ON_ACTION_UP,
                    bundleOf(Pair(ResultKeys.ON_ACTION_UP, dY))
                )
            }

            weatherIconsInfoImageView.setOnClickListener {
                childFragmentManager.commit { replace(R.id.root_layout, WeatherIconsInfoDialogFragment()) }
            }

            hourlyWeatherRecyclerView.adapter = forecastHourlyWeatherAdapter
            hourlyWeatherRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            dailyWeatherRecyclerView.adapter = forecastDailyWeatherAdapter
            dailyWeatherRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

            airQualityInfoImageView.setOnClickListener {
                childFragmentManager.commit { replace(R.id.root_layout, AirQualityIndexDialogFragment()) }
            }

            fineParticleQualityProgressView.setProgressBackgroundColor(requireContext().getColor(R.color.background_air_quality_color))
            ultraFineParticleQualityProgressView.setProgressBackgroundColor(requireContext().getColor(R.color.background_air_quality_color))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObserver() {
        super.setupObserver()

        viewModel.forecastWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { forecastWeather ->
                val hourlyWeathers = forecastWeather.hourlyWeathers.filter {
                    val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                        .withMinute(0)
                        .withSecond(0)
                        .withNano(0)

                    it.dateTime >= now
                }

                val nowHourlyWeather = hourlyWeathers.first()

                hourlyWeathers
                    .filter { it.dateTime.hour % 3 == 0 }
                    .groupBy { it.dateTime.dayOfMonth }
                    .flatMap { it.value + HourlyWeather.DIVIDER }
                    .dropLast(3) /* TODO: 3 -> 1 */
                    .toMutableList()
                    .apply {
                        if (first().dateTime.dayOfMonth > nowHourlyWeather.dateTime.dayOfMonth) {
                            add(0, nowHourlyWeather)
                            add(1, HourlyWeather.DIVIDER)
                        } else if (first() != nowHourlyWeather) {
                            add(0, nowHourlyWeather)
                        }
                    }
                    .let { forecastHourlyWeatherAdapter.submitList(it) }

                forecastDailyWeatherAdapter.submitList(forecastWeather.dailyWeathers)

                val uvIndex = forecastWeather.uvIndex

                val sunriseSunset = forecastWeather.sunriseSunset
                val sunriseInMinute = sunriseSunset.sunrise.let { it.hour * 60 + it.minute }
                val sunsetInMinute = sunriseSunset.sunset.let { it.hour * 60 + it.minute }
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).let { it.hour * 60 + it.minute }
                val max = sunsetInMinute - sunriseInMinute
                val progress = min(max, max(0, now - sunriseInMinute))

                binding.apply {
                    popValueTextView.text = "${nowHourlyWeather.probabilityOfPrecipitation}%"
                    uvraysValueTextView.text = "${uvIndex.grade} ${uvIndex.index}"

                    val dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    sunriseTimeTextView.text = dateTimeFormatter.format(sunriseSunset.sunrise)
                    sunsetTimeTextView.text = dateTimeFormatter.format(sunriseSunset.sunset)
                    sunriseSunsetProgressIndicator.max = max
                    sunriseSunsetProgressIndicator.progress = progress
                }
            }
        }

        viewModel.presentWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeather ->
                binding.apply {
                    fineParticleQualityTextView.text = presentWeather.fineParticleValue.toString()
                    fineParticleQualityProgressView.setProgress(presentWeather.fineParticleGrade.percentageOf(presentWeather.fineParticleValue))
                    fineParticleQualityProgressView.setProgressColor(requireContext().getColor(presentWeather.fineParticleGrade.getColorResId()))
                    fineParticleGradeTextView.text = presentWeather.fineParticleGrade.getDescription()

                    ultraFineParticleQualityTextView.text = presentWeather.ultraFineParticleValue.toString()
                    ultraFineParticleQualityProgressView.setProgress(presentWeather.ultraFineParticleGrade.percentageOf(presentWeather.ultraFineParticleValue))
                    ultraFineParticleQualityProgressView.setProgressColor(requireContext().getColor(presentWeather.ultraFineParticleGrade.getColorResId()))
                    ultraFineParticleGradeTextView.text = presentWeather.ultraFineParticleGrade.getDescription()

                    humidityValueTextView.text = "${presentWeather.humidity}%"
                    windTextView.text = descriptionOfWindDirection(presentWeather.windDirection)
                    windValueTextView.text = "${presentWeather.windSpeed} m/s"
                }
            }
        }
    }

    private fun descriptionOfWindDirection(windDirection: Int): String{
        return when (((windDirection + 22.5 * 0.5) / 22.5).toInt()) {
            0 -> "??????"
            1 -> "?????????"
            2 -> "?????????"
            3 -> "?????????"
            4 -> "??????"
            5 -> "?????????"
            6 -> "?????????"
            7 -> "?????????"
            8 -> "??????"
            9 -> "?????????"
            10 -> "?????????"
            11 -> "?????????"
            12 -> "??????"
            13 -> "?????????"
            14 -> "?????????"
            15 -> "?????????"
            16 -> "??????"
            else -> throw IllegalStateException()
        }
    }

    override fun onBackPressed(): Boolean {
        binding.scrollView.scrollTo(0, 0)
        return super.onBackPressed()
    }
}