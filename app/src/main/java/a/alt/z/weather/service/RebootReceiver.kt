package a.alt.z.weather.service

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

class RebootReceiver: BroadcastReceiver() {

    @SuppressLint("UnsafeProtectedBroadcastReceiver")
    override fun onReceive(context: Context, intent: Intent) {
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
}