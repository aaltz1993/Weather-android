package a.alt.z.weather.ui.main

import a.alt.z.weather.R
import a.alt.z.weather.databinding.ActivityMainBinding
import a.alt.z.weather.model.location.Coordinate
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.LocationFragment
import a.alt.z.weather.ui.splash.SplashFragment
import a.alt.z.weather.ui.weather.WeatherFragment
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.permissionsGranted
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.commit
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.abs

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel: MainViewModel by viewModels()

    private val permissions = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult)

    @Inject lateinit var locationProvider: FusedLocationProviderClient

    private fun onActivityResult(result: Map<String, Boolean>) {
        if (permissionsGranted(permissions.toList())) {
            locationProvider.lastLocation
                .addOnSuccessListener { viewModel.addDeviceLocation(Coordinate(it.latitude, it.longitude)) }
                .addOnFailureListener {  }
        } else {
            supportFragmentManager.commit { replace(R.id.fragment_container, LocationFragment()) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.commit { replace(R.id.fragment_container, SplashFragment()) }

        initView()

        setupObserver()
    }

    private fun initView() {
        binding.apply {
            (viewPager.getChildAt(0) as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
            viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
                override fun onPageScrolled(
                    position: Int,
                    positionOffset: Float,
                    positionOffsetPixels: Int
                ) {
                    super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                    indicatorsLayout.children.filter { it.isVisible }.forEachIndexed { index, view ->
                        val selected = position == index

                        val colorResId = if (selected) {
                            R.color.indicator_selected_color
                        } else {
                            R.color.indicator_background_color
                        }

                        if (view is ImageView) {
                            view.setColorFilter(ContextCompat.getColor(this@MainActivity, colorResId))
                        } else {
                            view.backgroundTintList = ContextCompat.getColorStateList(this@MainActivity, colorResId)
                        }
                    }
                }
            })

            loadingLayout.setOnClickListener { /* block user input */ }
        }
    }

    private fun setupObserver() {
        viewModel.locations.observe(this) { result ->
            result.successOrNull()?.let { locations ->
                when {
                    locations.isEmpty() -> {
                        if (permissionsGranted(permissions.toList())) {
                            locationProvider.lastLocation
                                .addOnSuccessListener { viewModel.addDeviceLocation(Coordinate(it.latitude, it.longitude)) }
                                .addOnFailureListener { /* TODO */ }
                        } else {
                            requestPermissions.launch(permissions)
                        }
                    }
                    else -> {
                        binding.apply {
                            currentLocationIndicator.isVisible = locations.any { it.isDeviceLocation }
                            val size = locations.filterNot { it.isDeviceLocation }.size
                            indicator1.isVisible = size > 0
                            indicator2.isVisible = size > 1
                            indicator3.isVisible = size > 2
                            indicator4.isVisible = size > 3

                            viewPager.adapter = MainPageAdapter(
                                this@MainActivity,
                                locations.sortedByDescending { it.isDeviceLocation }.map { WeatherFragment(it) }
                            )
                        }

                        locations.find { it.isDeviceLocation }?.let { currentLocation ->
                            locationProvider.lastLocation
                                .addOnSuccessListener { lastLocation ->
                                    updateCurrentLocationIfNeeded(
                                        currentLocation.latitude, currentLocation.longitude,
                                        lastLocation.latitude, lastLocation.longitude
                                    )
                                }
                                .addOnFailureListener { /* TODO */ }
                        }
                    }
                }
            }
        }

        supportFragmentManager.setFragmentResultListener(RequestKeys.PAGEABLE, this) { _, result ->
            binding.apply {
                val pageable = result.getBoolean(ResultKeys.PAGEABLE)
                viewPager.isUserInputEnabled = pageable
                indicatorsLayout.isVisible = pageable
            }
        }

        supportFragmentManager.setFragmentResultListener(RequestKeys.DATA_LOADED, this) { _, result ->
            val dataLoaded = result.getBoolean(ResultKeys.DATA_LOADED)

            val child = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (child is SplashFragment) {
                supportFragmentManager.setFragmentResult(
                    RequestKeys.DATA_LOADED_SPLASH,
                    bundleOf(Pair(ResultKeys.DATA_LOADED_SPLASH, dataLoaded))
                )
            } else {
                binding.loadingLayout.isVisible = !dataLoaded
            }
        }
    }

    private fun updateCurrentLocationIfNeeded(
        oldLatitude: Double, oldLongitude: Double,
        newLatitude: Double, newLongitude: Double
    ) {
        val distances = FloatArray(3)
        Location.distanceBetween(
            oldLatitude, oldLongitude,
            newLatitude, newLongitude,
            distances
        )

        val distance = distances.firstOrNull() ?: 0F
        if (abs(distance) > 500F) {
            viewModel.addDeviceLocation(Coordinate(newLatitude, newLongitude))
        }
    }

    override fun onBackPressed() {
        supportFragmentManager.fragments.forEach { fragment ->
            (fragment as? BaseFragment)?.onBackPressed()
                ?.takeIf { it }
                ?.let { return }
        }

        super.onBackPressed()
    }
}