package a.alt.z.weather.initializer

import a.alt.z.weather.BuildConfig
import a.alt.z.weather.utils.debug.WeatherDebugTree
import android.content.Context
import androidx.startup.Initializer
import timber.log.Timber

class TimberInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        if (BuildConfig.DEBUG) {
            Timber.plant(WeatherDebugTree())
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}