package a.alt.z.weather.data.preferences

import a.alt.z.weather.data.preferences.preference.BooleanPreference
import a.alt.z.weather.utils.extensions.dataStore
import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.preference.PreferenceManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherPreferencesStorage @Inject constructor(
    @ApplicationContext context: Context
) : PreferencesStorage {

    private val sharedPreferences: SharedPreferences by lazy {
        PreferenceManager.getDefaultSharedPreferences(context)
    }

    private val dataStore: DataStore<Preferences> = context.applicationContext.dataStore

    override var skipOnboarding: Boolean by BooleanPreference(sharedPreferences, "skipOnboarding", false)

    override var notificationsOn: Boolean by BooleanPreference(sharedPreferences, "notificationsOn", false)

    override var locationServiceOn: Flow<Boolean> = dataStore.data.map { preferences -> preferences[LOCATION_SERVICE_ON] ?: false }

    override suspend fun setLocationServiceOn(locationServiceOn: Boolean) {
        dataStore.edit { preferences ->
            preferences[LOCATION_SERVICE_ON] = locationServiceOn
        }
    }

    companion object {
        private val LOCATION_SERVICE_ON = booleanPreferencesKey("locationServiceOn")
    }
}