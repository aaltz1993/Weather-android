package a.alt.z.weather.ui.onboarding

import a.alt.z.weather.databinding.ItemDailyForecastOnboardingBinding
import a.alt.z.weather.model.weather.DailyWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.utils.extensions.layoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId

class OnboardingDailyForecastAdapter: RecyclerView.Adapter<OnboardingDailyForecastViewHolder>() {

    private val dailyWeathers = listOf(
        DailyWeather(
            LocalDate.now(ZoneId.of("Asia/Seoul")), 0, 0,
            Sky.CLOUDY, Sky.CLEAR,
            PrecipitationType.NONE, PrecipitationType.NONE,
            20, 0
        ),
        DailyWeather(
            LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(1), 0, 0,
            Sky.CLEAR, Sky.CLEAR,
            PrecipitationType.NONE, PrecipitationType.NONE,
            0, 0
        ),
        DailyWeather(
            LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(2), 0, 0,
            Sky.OVERCAST, Sky.OVERCAST,
            PrecipitationType.RAIN, PrecipitationType.RAIN,
            60, 60
        ),
        DailyWeather(
            LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(3), 0, 0,
            Sky.OVERCAST, Sky.OVERCAST,
            PrecipitationType.RAIN, PrecipitationType.NONE,
            60, 0
        )
    )

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OnboardingDailyForecastViewHolder {
        return ItemDailyForecastOnboardingBinding
            .inflate(parent.layoutInflater, parent, false)
            .let { OnboardingDailyForecastViewHolder(it) }
    }

    override fun onBindViewHolder(holder: OnboardingDailyForecastViewHolder, position: Int) {
        holder.bind(dailyWeathers[position])
    }

    override fun getItemCount(): Int {
        return 4
    }
}