package a.alt.z.weather.data.preferences.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class BooleanPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: Boolean
) : ReadWriteProperty<Any, Boolean> {

    override fun getValue(thisRef: Any, property: KProperty<*>): Boolean = preferences.getBoolean(name, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = preferences.edit { putBoolean(name, value) }
}