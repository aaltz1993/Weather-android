package a.alt.z.weather.ui

import a.alt.z.weather.R
import a.alt.z.weather.data.api.service.SunriseSunsetService
import a.alt.z.weather.databinding.ActivityMainBinding
import a.alt.z.weather.model.location.Coordinate
import a.alt.z.weather.ui.base.BaseFragment
import a.alt.z.weather.ui.location.LocationFragment
import a.alt.z.weather.ui.splash.SplashFragment
import a.alt.z.weather.ui.weather.WeatherFragment
import a.alt.z.weather.utils.extensions.permissionsGranted
import a.alt.z.weather.utils.extensions.viewBinding
import a.alt.z.weather.utils.result.successOrNull
import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.location.LocationManager
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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import timber.log.debug
import javax.inject.Inject

@AndroidEntryPoint
@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel: MainViewModel by viewModels()

    private val permissions = arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION)

    private val requestPermissions = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions(), ::onActivityResult)

    private val locationProvider: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

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
                                .addOnFailureListener {  }
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
                                locations.sortedByDescending { it.isDeviceLocation }.map { WeatherFragment.newInstance(it) }
                            )
                        }
                    }
                }
            }
        }

        supportFragmentManager.setFragmentResultListener("isPageableRequestKey", this) { _, result ->
            val isPageable = result.getBoolean("isPageable")
            binding.apply {
                viewPager.isUserInputEnabled = isPageable
                indicatorsLayout.isVisible = isPageable
            }
            Timber.debug { "MainActivity::isPageable=$isPageable" }
        }

        supportFragmentManager.setFragmentResultListener("isDataReadyRequestKey", this) { _, result ->
            val isDataReady = result.getBoolean("isDataReady")

            val child = supportFragmentManager.findFragmentById(R.id.fragment_container)

            if (child == null) {
                binding.loadingLayout.isVisible = !isDataReady
            } else {
                supportFragmentManager.setFragmentResult("dataLoadedRequestKey", bundleOf(Pair("dataLoaded", isDataReady)))
            }
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