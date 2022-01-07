package a.alt.z.weather.data.preferences.preference

import a.alt.z.weather.model.appwidget.AppWidgetConfigure
import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class AppWidgetConfigurePreference(
    private val preferences: SharedPreferences,
    private val name: String
) : ReadWriteProperty<Any, List<AppWidgetConfigure>> {

    private val gson = Gson()

    override fun getValue(thisRef: Any, property: KProperty<*>): List<AppWidgetConfigure> {
        return gson.fromJson(
            preferences.getString(name, null),
            object: TypeToken<List<AppWidgetConfigure>>() {}.type
        ) ?: emptyList()
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: List<AppWidgetConfigure>) {
        preferences.edit { putString(name, gson.toJson(value)) }
    }
}