package a.alt.z.weather.ui.weather

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherBinding
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.*
import a.alt.z.weather.utils.result.successOrNull
import android.os.Bundle
import android.view.animation.LinearInterpolator
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import timber.log.debug
import kotlin.math.max
import kotlin.math.min

@AndroidEntryPoint
class WeatherFragment : BaseFragment(R.layout.fragment_weather) {

    private val binding by viewBinding(FragmentWeatherBinding::bind)

    private val viewModel: WeatherViewModel by viewModels()

    private var updatedTimeText = false
    
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
                if (!updatedTimeText) {
                    updateTimeTextIfNeeded()
                    updatedTimeText = true
                }

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
                    updatedTimeText = false

                    if (presentWeatherTopGuideline.guidePercent > 0.2) {
                        rootLayout.updateWithDelayedTransition {
                            setGuidelinePercent(R.id.present_weather_top_guideline, .25F)
                            setGuidelinePercent(R.id.present_weather_bottom_guideline, 1.25F)
                        }

                        updateIconImageView.animate()
                            .rotationBy(360F)
                            .setDuration(2000L)
                            .setInterpolator(LinearInterpolator())
                            .start()

                        val location = requireNotNull(viewModel.location.value)

                        viewModel.getPresentWeather(location)
                    } else {
                        rootLayout.updateWithDelayedTransition(duration = 500L) {
                            setGuidelinePercent(R.id.present_weather_top_guideline, minimumGuidePercent)
                            setGuidelinePercent(R.id.present_weather_bottom_guideline, maximumGuidePercent)
                        }

                        setPageable(true)
                    }
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

    private fun updateTimeTextIfNeeded() {
        val updatedAt = viewModel.presentWeather.value?.successOrNull()?.updatedAt

        if (updatedAt != null) {
            val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

            binding.updateTimeTextView.text = when {
                now.hour - updatedAt.hour > 0 -> "마지막 업데이트 ${now.hour - updatedAt.hour}시간 전"
                now.minute - updatedAt.minute == 0 -> "마지막 업데이트 방금"
                else -> "마지막 업데이트 ${now.minute - updatedAt.minute}분 전"
            }
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
            if (binding.presentWeatherTopGuideline.guidePercent == .25F) {
                return@observe
            }

            parentFragmentManager.setFragmentResult(
                RequestKeys.DATA_LOADED,
                bundleOf(Pair(ResultKeys.DATA_LOADED, dataLoaded))
            )
        }

        viewModel.presentWeather.observe(viewLifecycleOwner) { result ->
            result.successOrNull()?.let { presentWeather ->
                val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))
                val updatedAt = presentWeather.updatedAt

                binding.apply {
                    updateTimeTextView.text = when {
                        now.hour - updatedAt.hour > 0 -> "마지막 업데이트 ${now.hour - updatedAt.hour}시간 전"
                        now.minute - updatedAt.minute == 0 -> "마지막 업데이트 방금"
                        else -> "마지막 업데이트 ${now.minute - updatedAt.minute}분 전"
                    }

                    if (presentWeatherTopGuideline.guidePercent == .25F) {
                        rootLayout.updateWithDelayedTransition(duration = 500L) {
                            setGuidelinePercent(R.id.present_weather_top_guideline, minimumGuidePercent)
                            setGuidelinePercent(R.id.present_weather_bottom_guideline, maximumGuidePercent)
                        }

                        setPageable(true)
                    }
                }
            }
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