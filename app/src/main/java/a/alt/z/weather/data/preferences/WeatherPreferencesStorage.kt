package a.alt.z.weather.data.preferences

import a.alt.z.weather.data.preferences.preference.BooleanPreference
import a.alt.z.weather.utils.extensions.dataStore
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class WeatherPreferencesStorage @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesStorage {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val dataStore: DataStore<Preferences> = context.applicationContext.dataStore

    override var notificationsOn: Boolean by BooleanPreference(sharedPreferences, "notificationsOn", false)

    override var locationServiceOn: Boolean by BooleanPreference(sharedPreferences, "locationServiceOn", false)
}