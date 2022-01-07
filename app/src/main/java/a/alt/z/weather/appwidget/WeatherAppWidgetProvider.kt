package a.alt.z.weather.appwidget

import a.alt.z.weather.R
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.model.appwidget.AppWidgetData
import a.alt.z.weather.model.appwidget.AppWidgetUIMode
import a.alt.z.weather.ui.main.MainActivity
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.content.ContextCompat
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

class WeatherAppWidgetProvider: AppWidgetProvider() {

    private val job = SupervisorJob()

    private val coroutineScope = CoroutineScope(Dispatchers.Main + job)

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIDs: IntArray
    ) {
        val appWidgetDataManager = AppWidgetDataManager.getInstance(context)

        coroutineScope.launch {
            for (appWidgetID in appWidgetIDs) {
                val appWidgetData = withContext(Dispatchers.IO) {
                    appWidgetDataManager.getAppWidgetData(appWidgetID)
                }

                appWidgetData?.let {
                    updateAppWidget(context, appWidgetManager, appWidgetData)
                }
            }
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        super.onDeleted(context, appWidgetIds)

        coroutineScope.launch(Dispatchers.IO) {
            appWidgetIds.forEach {
                AppWidgetDataManager.getInstance(context).removeAppWidgetConfigure(it)
            }
        }
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)

        coroutineScope.launch {
            AppWidgetDataManager.getInstance(context).clearAppWidgetConfigures()
        }
    }

    companion object {
        const val ACTION_RELOAD = "a.alt.z.weather.widget.action.RELOAD"
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetData: AppWidgetData
) {
    val appWidgetID = appWidgetData.configure.widgetID

    val (backgroundColor, color) = when (appWidgetData.configure.uiMode) {
        AppWidgetUIMode.LIGHT -> Pair(ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.black))
        AppWidgetUIMode.DARK -> Pair(ContextCompat.getColor(context, R.color.black), ContextCompat.getColor(context, R.color.white))
        AppWidgetUIMode.ADAPTIVE -> {
            if (appWidgetData.isNight) {
                Pair(ContextCompat.getColor(context, R.color.black), ContextCompat.getColor(context, R.color.white))
            } else {
                Pair(ContextCompat.getColor(context, R.color.white), ContextCompat.getColor(context, R.color.black))
            }
        }
    }

    val remoteViews = RemoteViews(context.packageName, R.layout.widget_weather).apply {
        setInt(R.id.background_image_view, "setColorFilter", backgroundColor)
        setInt(R.id.background_image_view, "setImageAlpha", (255 - 255 * (appWidgetData.configure.transparency / 100F)).toInt())

        setTextViewText(R.id.address_text_view, appWidgetData.locationName)
        setTextColor(R.id.address_text_view, color)

        setInt(R.id.current_location_icon_image_view, "setColorFilter", color)

        setTextViewText(R.id.temperature_text_view, appWidgetData.temperature.toString())
        setTextColor(R.id.temperature_text_view, color)

        setInt(R.id.temperature_degree_image_view, "setColorFilter", color)

        setTextViewText(R.id.weather_description_text_view, appWidgetData.weatherDescription)
        setTextColor(R.id.weather_description_text_view, color)

        setImageViewResource(R.id.weather_icon_image_view, appWidgetData.weatherIconResId)

        setTextColor(R.id.fine_particle_name_text_view, color)

        setTextViewText(R.id.fine_particle_grade_description_text_view, appWidgetData.fineParticleGradeName)
        setTextColor(R.id.fine_particle_grade_description_text_view, ContextCompat.getColor(context, appWidgetData.fineParticleGradeTextColor))

        val intent = Intent(context, MainActivity::class.java)

        PendingIntent
            .getActivity(context, 0, intent, 0)
            .let { pendingIntent -> setOnClickPendingIntent(R.id.content_layout, pendingIntent) }
    }

    appWidgetManager.updateAppWidget(appWidgetID, remoteViews)
}