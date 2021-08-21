package a.alt.z.weather.initializer

import android.content.Context
import androidx.startup.Initializer
import timber.log.LogcatTree
import timber.log.Timber

class TimberInitializer: Initializer<Unit> {

    override fun create(context: Context) {
        Timber.plant(LogcatTree(TAG))
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }

    companion object {
        private const val TAG = "aaltz1993"
    }
}