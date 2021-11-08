package a.alt.z.weather.utils.extensions

import android.content.Context
import android.content.pm.PackageManager
import android.util.TypedValue
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.google.android.material.snackbar.Snackbar

/* permission */
fun Context.permissionGranted(permission: String): Boolean {
    return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
}

fun Context.permissionsGranted(permissions: List<String>): Boolean {
    return permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
}

fun Context.permissionsGranted(vararg permissions: String): Boolean {
    return permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }
}

/* dimension */
fun Context.pixelsOf(dp: Number): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), resources.displayMetrics)

/* toast & snackbar */
fun Context.showToast(duration: Int = Toast.LENGTH_SHORT, text: (() -> String)) {
    Toast.makeText(this, text(), duration).show()
}

fun Context.showSnackbar(view: View, duration: Int = Snackbar.LENGTH_SHORT, text: (() -> String)) {
    Snackbar.make(view, text(), duration).show()
}

/* DataStore */
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather")

/* IMM */
val Context.inputMethodManager get() = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager