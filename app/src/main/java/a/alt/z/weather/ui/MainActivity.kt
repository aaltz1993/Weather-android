package a.alt.z.weather.ui

import a.alt.z.weather.R
import a.alt.z.weather.ui.main.MainFragment
import a.alt.z.weather.utils.LambertConformalConicProjection
import android.annotation.SuppressLint
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import timber.log.debug
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val locationManager: LocationManager by lazy {
        applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    private val geocoder: Geocoder by lazy {
        Geocoder(applicationContext, Locale.KOREA)
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult)

    @SuppressLint("MissingPermission")
    private fun onActivityResult(permissions: Map<String, Boolean>) {
        val permissionsGranted = permissions.all { it.value }

        if (permissionsGranted) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                if (location != null) {
                    val address = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                    Timber.debug { "address: $address" }

                    val grid = LambertConformalConicProjection.transform(location.latitude, location.longitude)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()
    }

    private fun initView() {
        supportFragmentManager.commit { replace(R.id.fragment_container, MainFragment()) }
    }

}