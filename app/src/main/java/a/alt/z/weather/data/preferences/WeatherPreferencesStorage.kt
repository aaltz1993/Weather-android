package a.alt.z.weather.data.preferences

import a.alt.z.weather.data.preferences.preference.BooleanPreference
import a.alt.z.weather.utils.extensions.dataStore
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.preference.PreferenceManager

class WeatherPreferencesStorage(context: Context) : PreferencesStorage {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val dataStore: DataStore<Preferences> = context.applicationContext.dataStore

    override var notificationsOn: Boolean by BooleanPreference(sharedPreferences, "notificationsOn", false)
}