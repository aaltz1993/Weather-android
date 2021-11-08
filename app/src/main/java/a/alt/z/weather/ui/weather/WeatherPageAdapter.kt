package a.alt.z.weather.ui.weather

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeatherPageAdapter(
    fragment: Fragment
): FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PresentWeatherFragment()
            1 -> ForecastWeatherFragment()
            else -> throw IllegalStateException()
        }
    }
}