package a.alt.z.weather

import a.alt.z.weather.data.database.dao.SunriseSunsetDao
import a.alt.z.weather.service.AlarmReceiver
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatDelegate
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.*
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
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
            } else {
                val default = getDefaultSunriseSunset()
                if (now in default[0]..default[1]) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
            }
        }
    }

    private fun getDefaultSunriseSunset(): List<LocalDateTime> {
        val today = LocalDate.now(ZoneId.of("Asia/Seoul"))
        return when (today.month) {
            Month.JANUARY -> listOf(
                LocalDateTime.of(today, LocalTime.of(7, 45)),
                LocalDateTime.of(today, LocalTime.of(17, 30))
            )
            Month.FEBRUARY -> listOf(
                LocalDateTime.of(today, LocalTime.of(7, 28)),
                LocalDateTime.of(today, LocalTime.of(18, 3))
            )
            Month.MARCH -> listOf(
                LocalDateTime.of(today, LocalTime.of(6, 53)),
                LocalDateTime.of(today, LocalTime.of(18, 32))
            )
            Month.APRIL -> listOf(
                LocalDateTime.of(today, LocalTime.of(6, 8)),
                LocalDateTime.of(today, LocalTime.of(18, 32))
            )
            Month.MAY -> listOf(
                LocalDateTime.of(today, LocalTime.of(5, 30)),
                LocalDateTime.of(today, LocalTime.of(19, 27))
            )
            Month.JUNE -> listOf(
                LocalDateTime.of(today, LocalTime.of(5, 12)),
                LocalDateTime.of(today, LocalTime.of(19, 51))
            )
            Month.JULY -> listOf(
                LocalDateTime.of(today, LocalTime.of(5, 19)),
                LocalDateTime.of(today, LocalTime.of(19, 55))
            )
            Month.AUGUST -> listOf(
                LocalDateTime.of(today, LocalTime.of(5, 41)),
                LocalDateTime.of(today, LocalTime.of(19, 33))
            )
            Month.SEPTEMBER -> listOf(
                LocalDateTime.of(today, LocalTime.of(6, 7)),
                LocalDateTime.of(today, LocalTime.of(18, 50))
            )
            Month.OCTOBER -> listOf(
                LocalDateTime.of(today, LocalTime.of(6, 33)),
                LocalDateTime.of(today, LocalTime.of(18, 5))
            )
            Month.NOVEMBER -> listOf(
                LocalDateTime.of(today, LocalTime.of(7, 4)),
                LocalDateTime.of(today, LocalTime.of(17, 28))
            )
            Month.DECEMBER -> listOf(
                LocalDateTime.of(today, LocalTime.of(7, 33)),
                LocalDateTime.of(today, LocalTime.of(17, 15))
            )
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