package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentOnboarding3Binding
import a.alt.z.weather.databinding.ItemHourlyForecastOnboardingBinding
import a.alt.z.weather.model.weather.HourlyWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.viewBinding
import android.graphics.PointF
import android.util.DisplayMetrics
import android.view.LayoutInflater
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.threeten.bp.LocalDateTime

class Onboarding3Fragment: BaseFragment(R.layout.fragment_onboarding3) {

    private val binding by viewBinding(FragmentOnboarding3Binding::bind)

    private var canScrollHorizontally = true

    override fun initView() {
        binding.apply {
            hourlyWeatherRecyclerView.adapter = OnboardingHourlyForecastAdapter()
            hourlyWeatherRecyclerView.layoutManager = object: LinearLayoutManager(requireContext(), HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return canScrollHorizontally
                }
            }

            dailyWeatherRecyclerView.adapter = OnboardingDailyForecastAdapter()
            dailyWeatherRecyclerView.layoutManager = object: LinearLayoutManager(requireContext(), HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(250L)

            binding.apply {
                onboardingDescriptionTextView.animate()
                    .alpha(1F)
                    .translationY(0F)
                    .setDuration(500L)
                    .start()

                onboardingTitleTextView.animate()
                    .alpha(1F)
                    .setDuration(500L)
                    .withEndAction {
                        context?.let {
                            val smoothScroller = object: LinearSmoothScroller(it) {
                                override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                                    return 200F / displayMetrics.densityDpi
                                }
                                override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
                                    return (hourlyWeatherRecyclerView.layoutManager as LinearLayoutManager).computeScrollVectorForPosition(targetPosition)
                                }
                            }
                            smoothScroller.targetPosition = 10
                            hourlyWeatherRecyclerView.layoutManager?.startSmoothScroll(smoothScroller)
                        }
                    }
                    .start()

                delay(1000L)
                canScrollHorizontally = false
            }
        }
    }
}