package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.PreviewFragment
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.*
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.debug
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val binding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = requireArguments().getParcelable<Location>(ARG_LOCATION)

        requireNotNull(location)

        viewModel.setLocation(location)
    }

    override fun initView() {
        binding.apply {
            presentWeatherFragmentContainer.onPullDown = { dY ->
                rootLayout.update {
                    setGuidelinePercent(
                        R.id.present_weather_top_guideline,
                        min(max(presentWeatherTopGuideline.guidePercent + dY, 0F), 0.25F)
                    )
                    setGuidelinePercent(
                        R.id.present_weather_bottom_guideline,
                        min(max(presentWeatherBottomGuideline.guidePercent + dY, 1F), 1.25F)
                    )
                }

                parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, false)))
            }

            presentWeatherFragmentContainer.onSwipeUp = { dY ->
                rootLayout.update {
                    val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, 0F), 1F)
                    val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, 1F), 2F)
                    setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                    setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                    setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                    setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)

                    setAlpha(R.id.weather_image_view, max(0F, (2 * top - 1)))
                }

                parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, false)))
            }

            presentWeatherFragmentContainer.onSwipe = {
                val isIdle = presentWeatherTopGuideline.guidePercent == 0F && forecastWeatherTopGuideline.guidePercent == 1F

                parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, isIdle)))
            }

            presentWeatherFragmentContainer.onActionUp = { dY, interceptPullDown ->
                if (interceptPullDown) {
                    rootLayout.updateWithDelayedTransition(duration = 1000L) {
                        setGuidelinePercent(R.id.present_weather_top_guideline, 0F)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, 1F)

                        parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, true)))
                    }
                } else {
                    rootLayout.updateWithDelayedTransition {
                        val (top, bottom) = if (forecastWeatherTopGuideline.guidePercent < 0.5F || dY < 0F) {
                            Pair(0F, 1F)
                        } else {
                            Pair(1F, 2F)
                        }
                        setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                        setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                        setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)

                        weatherImageView.animate()
                            .alpha(bottom - 1F)
                            .setDuration(250L)
                            .start()

                        parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, top == 1F)))
                    }


                }
            }

            childFragmentManager.commit { replace(R.id.present_weather_fragment_container, PresentWeatherFragment()) }

            childFragmentManager.commit { replace(R.id.forecast_weather_fragment_container, ForecastWeatherFragment()) }
        }
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.isDataReady.observe(viewLifecycleOwner) { isDataReady ->
            parentFragmentManager.setFragmentResult(RequestKeys.DATA_READY, bundleOf(Pair(ResultKeys.DATA_READY, isDataReady)))
        }

        viewModel.presentWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeather ->
                val resId = imageResIdOf(
                    presentWeather.sky,
                    presentWeather.precipitationType,
                    presentWeather.temperature
                )

                binding.weatherImageView.setImageResource(resId)
            }
        }

        childFragmentManager.setFragmentResultListener(RequestKeys.ON_ACTION_MOVE, viewLifecycleOwner) { _, result ->
            val dY = result.getFloat(ResultKeys.ON_ACTION_MOVE)

            if (dY < 0.03) {
                binding.apply {
                    rootLayout.update {
                        val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, 0F), 1F)
                        val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, 1F), 2F)
                        setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                        setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                        setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)

                        setAlpha(R.id.weather_image_view, max(0F, (2 * top - 1)))
                    }
                }
            }
        }

        childFragmentManager.setFragmentResultListener(RequestKeys.ON_ACTION_MOVE, viewLifecycleOwner) { _, result ->
            val dY = result.getFloat(ResultKeys.ON_ACTION_UP)

            binding.apply {
                rootLayout.updateWithDelayedTransition(duration = 500L) {
                    val (top, bottom) = if (forecastWeatherTopGuideline.guidePercent < 0.05F || dY < 0F) {
                        Pair(0F, 1F)
                    } else {
                        Pair(1F, 2F)
                    }
                    setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                    setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                    setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                    setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)

                    weatherImageView.animate()
                        .alpha(bottom - 1F)
                        .setDuration(250L)
                        .start()

                    parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, top == 1F)))
                }
            }
        }
    }

    private fun imageResIdOf(sky: Sky, precipitationType: PrecipitationType, temperature: Int): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> {
                        when {
                            temperature <= 5 -> R.drawable.image_clear_very_cold
                            temperature in 6..11 -> R.drawable.image_clear_cold
                            temperature in 12..22 -> R.drawable.image_clear_warm
                            else -> R.drawable.image_clear_warm
                        }
                    }
                    Sky.CLOUDY -> {
                        when {
                            temperature <= 5 -> R.drawable.image_cloudy_very_cold
                            temperature in 6..11 -> R.drawable.image_cloudy_cold
                            temperature in 12..22 -> R.drawable.image_cloudy_warm
                            else -> R.drawable.image_cloudy_warm
                        }
                    }
                    Sky.OVERCAST -> {
                        when {
                            temperature <= 5 -> R.drawable.image_overcast_very_cold
                            temperature in 6..11 -> R.drawable.image_overcast_cold
                            temperature in 12..22 -> R.drawable.image_overcast_warm
                            else -> R.drawable.image_overcast_warm
                        }
                    }
                }
            }
            PrecipitationType.RAIN -> {
                when {
                    temperature <= 5 -> R.drawable.image_rain_very_cold
                    temperature in 6..11 -> R.drawable.image_rain_cold
                    temperature in 12..22 -> R.drawable.image_rain_warm
                    else -> R.drawable.image_rain_warm
                }
            }
            PrecipitationType.SNOW -> R.drawable.image_snow
            PrecipitationType.RAIN_SNOW -> R.drawable.image_rain_snow
            PrecipitationType.SHOWER -> {
                when {
                    temperature <= 5 -> R.drawable.image_shower_very_cold
                    temperature in 6..11 -> R.drawable.image_shower_cold
                    temperature in 12..22 -> R.drawable.image_shower_warm
                    else -> R.drawable.image_shower_warm
                }
            }
        }
    }

    override fun onBackPressed(): Boolean {
        if (binding.forecastWeatherTopGuideline.guidePercent < 1F) {
            binding.rootLayout.updateWithDelayedTransition(duration = 500L) {
                setGuidelinePercent(R.id.present_weather_top_guideline, 0F)
                setGuidelinePercent(R.id.present_weather_bottom_guideline, 1F)
                setGuidelinePercent(R.id.forecast_weather_top_guideline, 1F)
                setGuidelinePercent(R.id.forecast_weather_bottom_guideline, 2F)

                parentFragmentManager.setFragmentResult(RequestKeys.PAGEABLE, bundleOf(Pair(ResultKeys.PAGEABLE, true)))
            }
            return true
        }

        return super.onBackPressed()
    }

    companion object {
        private const val ARG_LOCATION = "arg_location"

        fun newInstance(location: Location)  = WeatherFragment().apply {
            arguments = bundleOf(Pair(ARG_LOCATION, location))
        }
    }
}