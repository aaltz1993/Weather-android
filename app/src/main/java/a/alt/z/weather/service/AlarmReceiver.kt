package a.alt.z.weather.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.debug

@AndroidEntryPoint
class AlarmReceiver: HiltBroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)

        Timber.debug { "AlarmReceiver::onReceive" }

        val workManager = WorkManager.getInstance(context)
        val workRequest = OneTimeWorkRequestBuilder<SyncWeatherDataWorker>().build()
        workManager.enqueueUniqueWork(UNIQUE_WORK_NAME, ExistingWorkPolicy.KEEP, workRequest)
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "sync_weather_data_work"
    }
}

abstract class HiltBroadcastReceiver: BroadcastReceiver() {
    @CallSuper
    override fun onReceive(context: Context, intent: Intent) {}
}