package a.alt.z.weather.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.work.*
import dagger.hilt.android.AndroidEntryPoint
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime
import timber.log.Timber
import timber.log.debug
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class AlarmReceiver: HiltBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        WorkManager
            .getInstance(context)
            .enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequestBuilder<DownloadWeatherDataWorker>()
                    .setBackoffCriteria(BackoffPolicy.LINEAR, 1, TimeUnit.MINUTES)
                    .build()
            )

        scheduleDownloadTask(context)
    }

    private fun scheduleDownloadTask(context: Context) {
        val alarmManager = context.getSystemService(AlarmManager::class.java)

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

        val intent = Intent(context, AlarmReceiver::class.java)

        @SuppressLint("UnspecifiedImmutableFlag")
        val operation = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC, downloadAt.toInstant().toEpochMilli(), operation)
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "download_weather_data_work"
    }
}

abstract class HiltBroadcastReceiver: BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}