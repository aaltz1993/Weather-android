package a.alt.z.weather.ui

import a.alt.z.weather.R
import a.alt.z.weather.data.api.AirService
import a.alt.z.weather.data.api.WeatherService
import a.alt.z.weather.data.api.model.CurrentWeather
import a.alt.z.weather.utils.MapProjection
import a.alt.z.weather.utils.extensions.permissionGranted
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import timber.log.debug

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private val serviceKey = "CsK6Uk6eNuef3d4QeKizwBYW64z3uaruyV5eYXCh81jWC8HxnKNu3+kpJC7jXF2YUvx809yFR5a1wcvVJkXfNg=="

    private val weatherService: WeatherService by lazy {
        Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
                .client(
                        OkHttpClient.Builder()
                                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                                .build()
                )
                .addConverterFactory(
                        GsonConverterFactory.create()
                )
                .build()
                .create(WeatherService::class.java)
    }

    private val airService: AirService by lazy {
        Retrofit.Builder()
                .baseUrl("http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(AirService::class.java)
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult)


    private fun onActivityResult(permissions: Map<String, Boolean>) {
        val permissionsGranted = permissions.all { it.value }

        if (permissionsGranted) {
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }
    }

    private val locationManager: LocationManager by lazy {
        applicationContext.getSystemService(LOCATION_SERVICE) as LocationManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            if (permissionGranted(ACCESS_FINE_LOCATION) &&
                    permissionGranted(ACCESS_COARSE_LOCATION)) {
                val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

                location
                        ?.let { MapProjection.transform(it.latitude, it.longitude) }
                        ?.let { coordinate ->
                            val response = withContext(Dispatchers.IO) {
                                try {
                                    weatherService.getCurrentWeather(serviceKey, baseDate = 20210812, baseTime = 2400, nx = coordinate.x, ny = coordinate.y).response
                                } catch (exception: Exception) {
                                    null
                                }
                            }

                            response?.body?.items?.items
                                    ?.map { CurrentWeather.valueOf(it.category).apply { observedValue = it.observedValue } }
                                    ?.forEach { Timber.debug { "${it.label}, ${it.observedValue} ${it.unit}" } }
                        }
            } else {
                requestPermission.launch(
                        arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
                )
            }
        }

    }

    companion object {
        private const val RE = 6371.00877
        private const val GRID = 5.0
        private const val SLAT1 = 30.0
        private const val SLAT2 = 60.0
        private const val OLAT = 38.0
        private const val OLON = 126.0
        private const val XO = 210 / GRID
        private const val YO = 675 / GRID
        private const val DEGRAD = Math.PI / 180.0
    }
}