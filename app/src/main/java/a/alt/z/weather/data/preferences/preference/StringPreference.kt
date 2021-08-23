package a.alt.z.weather.data.preferences.preference

import android.content.SharedPreferences
import androidx.core.content.edit
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class StringPreference(
    private val preferences: SharedPreferences,
    private val name: String,
    private val defaultValue: String?
) : ReadWriteProperty<Any, String?> {

    override fun getValue(thisRef: Any, property: KProperty<*>): String? = preferences.getString(name, defaultValue)

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String?) = preferences.edit { putString(name, value) }
}