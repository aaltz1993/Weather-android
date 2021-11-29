package a.alt.z.weather

import a.alt.z.weather.data.database.dao.SunriseSunsetDao
import a.alt.z.weather.data.preferences.PreferencesStorage
import a.alt.z.weather.domain.usecase.others.GetSkipOnboardingUseCase
import a.alt.z.weather.service.AlarmReceiver
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import timber.log.debug
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application(), Configuration.Provider {

    @Inject lateinit var sunriseSunsetDao: SunriseSunsetDao

    @Inject lateinit var workerFactory: HiltWorkerFactory

    private val coroutineScope = MainScope()

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    override fun onCreate() {
        super.onCreate()

        applyNightMode()

        scheduleDownloadTask()
    }

    private fun applyNightMode() {
        coroutineScope.launch {
            val sunriseSunsetEntity = withContext(Dispatchers.IO) {
                sunriseSunsetDao.getSunriseSunset(LocalDate.now(ZoneId.of("Asia/Seoul")))
            }

            val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

            if (sunriseSunsetEntity != null) {
                val timeFormatter = DateTimeFormatter.ofPattern("HHmm")
                val sunrise = LocalDateTime.of(LocalDate.now(), LocalTime.from(timeFormatter.parse(sunriseSunsetEntity.sunrise)))
                val sunset = LocalDateTime.of(LocalDate.now(), LocalTime.from(timeFormatter.parse(sunriseSunsetEntity.sunset)))

                if (now in sunrise..sunset) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    private fun scheduleDownloadTask() {
        val alarmManager = getSystemService(AlarmManager::class.java)

        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        val downloadAt = if (now.hour > 2 || (now.hour == 2 && now.minute >= 30)) {
            now.plusDays(1)
        } else {
            now
        }.run {
            withHour(2)
                .withMinute(30)
                .withSecond(0)
                .withNano(0)
        }

        val intent = Intent(this, AlarmReceiver::class.java)

        @SuppressLint("UnspecifiedImmutableFlag")
        val operation = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, downloadAt.toInstant().toEpochMilli(), operation)
    }


}