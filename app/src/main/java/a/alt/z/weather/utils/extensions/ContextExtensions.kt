package a.alt.z.weather.utils.extensions

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

fun Context.permissionGranted(permission: String) = ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED