package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.*
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.debug
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val binding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    private val minimumGuidePercent = 0.001F
    private val maximumGuidePercent = 1.001F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val location = requireArguments().getParcelable<Location>(ARG_KEY_LOCATION)

        requireNotNull(location)

        viewModel.setLocation(location)
    }

    override fun initView() {
        binding.apply {
            presentWeatherFragmentContainer.onPullDown = { dY ->
                val top = min(max(presentWeatherTopGuideline.guidePercent + dY, minimumGuidePercent), 0.25F)
                val bottom = min(max(presentWeatherBottomGuideline.guidePercent + dY, maximumGuidePercent), 1.25F)

                rootLayout.update {
                    setGuidelinePercent(R.id.present_weather_top_guideline, top)
                    setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom)
                }

                setPageable(false)
            }

            presentWeatherFragmentContainer.onSwipeUp = { dY ->
                val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, minimumGuidePercent), maximumGuidePercent)
                val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, maximumGuidePercent), maximumGuidePercent + 1)

                beginTransition(top, bottom)

                setPageable(false)
            }

            presentWeatherFragmentContainer.onSwipe = {
                val isIdle = presentWeatherTopGuideline.guidePercent == minimumGuidePercent
                        && forecastWeatherTopGuideline.guidePercent == maximumGuidePercent

                setPageable(isIdle)
            }

            presentWeatherFragmentContainer.onActionUp = { dY, interceptPullDown ->
                if (interceptPullDown) {
                    rootLayout.updateWithDelayedTransition(duration = 500L) {
                        setGuidelinePercent(R.id.present_weather_top_guideline, minimumGuidePercent)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, maximumGuidePercent)
                    }

                    setPageable(true)
                } else {
                    val (top, bottom) = if (forecastWeatherTopGuideline.guidePercent < 0.5F || dY < 0F) {
                        Pair(minimumGuidePercent, maximumGuidePercent)
                    } else {
                        Pair(maximumGuidePercent, maximumGuidePercent + 1)
                    }

                    beginDelayedTransition(top, bottom)

                    setPageable(top == maximumGuidePercent)
                }
            }

            childFragmentManager.commit { replace(R.id.present_weather_fragment_container, PresentWeatherFragment()) }

            childFragmentManager.commit { replace(R.id.forecast_weather_fragment_container, ForecastWeatherFragment()) }
        }
    }

    private fun beginTransition(top: Float, bottom: Float) {
        binding.rootLayout.update {
            setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
            setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
            setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
            setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)
        }
    }

    private fun beginDelayedTransition(top: Float, bottom: Float) {
        binding.rootLayout.updateWithDelayedTransition(duration = 500L) {
            setGuidelinePercent(
                R.id.present_weather_top_guideline,
                if (top == maximumGuidePercent) minimumGuidePercent
                else minimumGuidePercent - 1F
            )
            setGuidelinePercent(
                R.id.present_weather_bottom_guideline,
                if (top == maximumGuidePercent) maximumGuidePercent
                else minimumGuidePercent
            )
            setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
            setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)
        }
    }

    private fun setPageable(pageable: Boolean) {
        parentFragmentManager.setFragmentResult(
            RequestKeys.PAGEABLE,
            bundleOf(Pair(ResultKeys.PAGEABLE, pageable))
        )
    }

    override fun setupObserver() {
        super.setupObserver()

        viewModel.dataLoaded.observe(viewLifecycleOwner) { dataLoaded ->
            parentFragmentManager.setFragmentResult(
                RequestKeys.DATA_LOADED,
                bundleOf(Pair(ResultKeys.DATA_LOADED, dataLoaded))
            )
        }

        childFragmentManager.setFragmentResultListener(RequestKeys.ON_ACTION_MOVE, viewLifecycleOwner) { _, result ->
            val dY = result.getFloat(ResultKeys.ON_ACTION_MOVE)

            if (dY < 0.03) {
                binding.apply {
                    val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, minimumGuidePercent), maximumGuidePercent)
                    val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, maximumGuidePercent), maximumGuidePercent + 1)

                    beginTransition(top, bottom)
                }
            }
        }

        childFragmentManager.setFragmentResultListener(RequestKeys.ON_ACTION_UP, viewLifecycleOwner) { _, result ->
            val dY = result.getFloat(ResultKeys.ON_ACTION_UP)

            binding.apply {
                val (top, bottom) = if (forecastWeatherTopGuideline.guidePercent < 0.05F || dY < 0F) {
                    Pair(minimumGuidePercent, maximumGuidePercent)
                } else {
                    Pair(maximumGuidePercent, maximumGuidePercent + 1)
                }

                beginDelayedTransition(top, bottom)

                setPageable(top == maximumGuidePercent)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        childFragmentManager
            .findFragmentById(R.id.forecast_weather_fragment_container)
            ?.let { it as? BaseFragment }
            ?.onBackPressed()

        if (binding.forecastWeatherTopGuideline.guidePercent < maximumGuidePercent) {
            beginDelayedTransition(maximumGuidePercent, maximumGuidePercent + 1)
            setPageable(true)
            return true
        }

        return super.onBackPressed()
    }

    companion object {
        private const val ARG_KEY_LOCATION = "argKeyLocation"

        fun instantiate(location: Location) = WeatherFragment().apply {
            arguments = bundleOf(Pair(ARG_KEY_LOCATION, location))
        }
    }
}