package a.alt.z.weather.data.preferences

import kotlinx.coroutines.flow.Flow

interface PreferencesStorage {

    var skipOnboarding: Boolean

    var notificationsOn: Boolean

    var locationServiceOn: Flow<Boolean>

    suspend fun setLocationServiceOn(locationServiceOn: Boolean)
}