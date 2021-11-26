package a.alt.z.weather.ui.dialog

import a.alt.z.weather.R
import a.alt.z.weather.databinding.FragmentWeatherIconsInfoBinding
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.utils.extensions.updateWithDelayedTransition
import a.alt.z.weather.utils.extensions.viewBinding
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class WeatherIconsInfoDialogFragment : BaseFragment(R.layout.fragment_weather_icons_info) {

    private val binding by viewBinding(FragmentWeatherIconsInfoBinding::bind)

    private val weatherIconsNotNight: List<WeatherIcon> = listOf(
        WeatherIcon(R.drawable.icon_clear, "맑음"),
        WeatherIcon(R.drawable.icon_cloudy, "구름많음"),
        WeatherIcon(R.drawable.icon_cloudy_rain, "구름많고 비"),
        WeatherIcon(R.drawable.icon_cloudy_snow, "구름많고 눈"),
        WeatherIcon(R.drawable.icon_cloudy_rain_snow, "구름많고\n비,눈"),
        WeatherIcon(R.drawable.icon_cloudy_shower, "구름많고\n소나기"),
        WeatherIcon(R.drawable.icon_overcast, "흐림"),
        WeatherIcon(R.drawable.icon_overcast_rain, "흐리고 비"),
        WeatherIcon(R.drawable.icon_overcast_snow, "흐리고 눈"),
        WeatherIcon(R.drawable.icon_overcast_rain_snow, "흐리고 비,눈"),
        WeatherIcon(R.drawable.icon_overcast_shower, "흐리고 소나기")
    )

    private val weatherIconsNight: List<WeatherIcon> = listOf(
        WeatherIcon(R.drawable.icon_clear_night, "맑음"),
        WeatherIcon(R.drawable.icon_cloudy_night, "구름많음"),
        WeatherIcon(R.drawable.icon_cloudy_rain_night, "구름많고 비"),
        WeatherIcon(R.drawable.icon_cloudy_snow_night, "구름많고 눈"),
        WeatherIcon(R.drawable.icon_cloudy_rain_snow_night, "구름많고\n비,눈"),
        WeatherIcon(R.drawable.icon_cloudy_shower_night, "구름많고\n소나기"),
        WeatherIcon(R.drawable.icon_overcast, "흐림"),
        WeatherIcon(R.drawable.icon_overcast_rain, "흐리고 비"),
        WeatherIcon(R.drawable.icon_overcast_snow, "흐리고 눈"),
        WeatherIcon(R.drawable.icon_overcast_rain_snow, "흐리고 비,눈"),
        WeatherIcon(R.drawable.icon_overcast_shower, "흐리고 소나기")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override fun initView() {
        binding.apply {
            rootLayout.setOnClickListener { dismissWithTransition() }

            closeButton.setOnClickListener { dismissWithTransition() }

            contentLayout.setOnClickListener { /* block user input */ }

            tabLayout.newTab()
                .apply {
                    view.background = null
                    view.setBackgroundColor(ContextCompat.getColor(requireContext(), android.R.color.transparent))
                }
                .setText(R.string.not_night)
                .let { tabLayout.addTab(it) }

            tabLayout.addTab(tabLayout.newTab().setText(R.string.night))

            tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    weatherIconsInfoViewPager.currentItem = tabLayout.selectedTabPosition
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })

            weatherIconsInfoViewPager.adapter = WeatherIconsInfoAdapter(listOf(weatherIconsNotNight, weatherIconsNight))
        }
    }

    private fun dismissWithTransition() {
        lifecycleScope.launch {
            binding.rootLayout.updateWithDelayedTransition {
                setGuidelinePercent(R.id.top_guideline, 1F)
                setGuidelinePercent(R.id.bottom_guideline, 2F)
            }

            delay(250L)

            parentFragmentManager.commit { remove(this@WeatherIconsInfoDialogFragment) }
        }
    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            delay(100L)

            binding.rootLayout.updateWithDelayedTransition(duration = 500L) {
                setGuidelinePercent(R.id.top_guideline, 0F)
                setGuidelinePercent(R.id.bottom_guideline, 1F)
            }

            delay(500L)

            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                binding.apply {
                    tabLayout.selectTab(tabLayout.getTabAt(1))
                    weatherIconsInfoViewPager.currentItem = 1
                }
            }
        }
    }

    override fun onDetach() {
        super.onDetach()

        requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
}