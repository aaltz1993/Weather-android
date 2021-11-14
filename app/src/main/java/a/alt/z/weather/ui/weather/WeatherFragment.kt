package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.PreviewFragment
import a.alt.z.weather.utils.extensions.*
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

                parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", false)))
            }

            presentWeatherFragmentContainer.onSwipeUp = { dY ->
                rootLayout.update {
                    val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, 0F), 1F)
                    val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, 1F), 2F)
                    setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                    setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                    setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                    setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)
                }

                parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", false)))
            }

            presentWeatherFragmentContainer.onSwipe = {
                val isIdle = presentWeatherTopGuideline.guidePercent == 0F && forecastWeatherTopGuideline.guidePercent == 1F

                parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", isIdle)))
            }

            presentWeatherFragmentContainer.onActionUp = { dY, interceptPullDown ->
                if (interceptPullDown) {
                    rootLayout.updateWithDelayedTransition(duration = 1000L) {
                        setGuidelinePercent(R.id.present_weather_top_guideline, 0F)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, 1F)

                        parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", true)))
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

                        parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", top == 1F)))
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
            parentFragmentManager.setFragmentResult("isDataReadyRequestKey", bundleOf(Pair("isDataReady", isDataReady)))
        }

        childFragmentManager.setFragmentResultListener("onPullDownRequestKey", viewLifecycleOwner) { _, result ->
            val dY = result.getFloat("onPullDown")

            if (dY < 0.03) {
                binding.apply {
                    rootLayout.update {
                        val top = min(max(forecastWeatherTopGuideline.guidePercent + dY, 0F), 1F)
                        val bottom = min(max(forecastWeatherBottomGuideline.guidePercent + dY, 1F), 2F)
                        setGuidelinePercent(R.id.present_weather_top_guideline, top - 1F)
                        setGuidelinePercent(R.id.present_weather_bottom_guideline, bottom - 1F)
                        setGuidelinePercent(R.id.forecast_weather_top_guideline, top)
                        setGuidelinePercent(R.id.forecast_weather_bottom_guideline, bottom)
                    }
                }
            }
        }

        childFragmentManager.setFragmentResultListener("onActionUpRequestKey", viewLifecycleOwner) { _, result ->
            val dY = result.getFloat("onActionUp")

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

                    parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", top == 1F)))
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

                parentFragmentManager.setFragmentResult("isPageableRequestKey", bundleOf(Pair("isPageable", true)))
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