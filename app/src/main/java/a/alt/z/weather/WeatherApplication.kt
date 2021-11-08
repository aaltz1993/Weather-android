package a.alt.z.weather

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime

@HiltAndroidApp
class WeatherApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        applyNightMode()
    }

    private fun applyNightMode() {
        val now = LocalDateTime.now()
        val sunrise = LocalDateTime.of(LocalDate.now(), LocalTime.of(6, 50))
        val sunset = LocalDateTime.of(LocalDate.now(), LocalTime.of(17, 40))

        if (now in sunrise..sunset) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }
}