package a.alt.z.weather

import a.alt.z.weather.service.AlarmReceiver
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.os.SystemClock
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.AlarmManagerCompat
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import org.threeten.bp.*
import org.threeten.bp.temporal.TemporalField
import timber.log.Timber
import timber.log.debug
import java.util.*
import javax.inject.Inject

@HiltAndroidApp
class WeatherApplication: Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        applyNightMode()

        scheduleSyncTask()
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

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun scheduleSyncTask() {
        val alarmManager = getSystemService(AlarmManager::class.java)

        val now = ZonedDateTime.now(ZoneId.of("Asia/Seoul"))

        val syncAt = if (now.hour > 2 || (now.hour == 2 && now.minute >= 30)) {
            now.plusDays(1)
        } else {
            now
        }.apply {
            withHour(14)
                .withMinute(30)
                .withSecond(0)
                .withNano(0)
        }.toInstant().toEpochMilli()

        val intent = Intent(this, AlarmReceiver::class.java)

        val operation = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        // alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, syncAt, operation)
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME, SystemClock.elapsedRealtime() + 1000L, operation)
    }

    @Inject lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }
}