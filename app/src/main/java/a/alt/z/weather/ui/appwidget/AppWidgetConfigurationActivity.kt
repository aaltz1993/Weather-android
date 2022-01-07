package a.alt.z.weather.ui.appwidget

import a.alt.z.weather.R
import a.alt.z.weather.appwidget.AppWidgetDataManager
import a.alt.z.weather.appwidget.updateAppWidget
import a.alt.z.weather.databinding.ActivityAppWidgetConfigurationBinding
import a.alt.z.weather.model.appwidget.AppWidgetConfigure
import a.alt.z.weather.model.appwidget.AppWidgetData
import a.alt.z.weather.model.appwidget.AppWidgetUIMode
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.utils.constants.RequestKeys
import a.alt.z.weather.utils.constants.ResultKeys
import a.alt.z.weather.utils.extensions.viewBinding
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import android.widget.SeekBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppWidgetConfigurationActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityAppWidgetConfigurationBinding::inflate)

    private val appWidgetID by lazy {
        intent?.extras
            ?.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
            ?: AppWidgetManager.INVALID_APPWIDGET_ID
    }

    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission(), ::onActivityResult)

    private fun onActivityResult(granted: Boolean) {
        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        WallpaperManager.getInstance(this)
            .drawable
            .let { Glide.with(this).load(it).into(binding.wallpaperImageView) }
    }

    private val appWidgetDataManager: AppWidgetDataManager by lazy {
        AppWidgetDataManager.getInstance(this)
    }

    private lateinit var location: Location
    private lateinit var presentWeather: PresentWeather

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(RESULT_CANCELED)
        setContentView(binding.root)

        if (appWidgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }

        if (ActivityCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            WallpaperManager.getInstance(this)
                .drawable
                .let { Glide.with(this).load(it).into(binding.wallpaperImageView) }
        } else {
            requestPermission.launch(READ_EXTERNAL_STORAGE)
        }

        lifecycleScope.launch {
            appWidgetDataManager.applyNightMode()
        }

        initViews()

        setupObserver()

        supportFragmentManager.commit {
            replace(R.id.root_layout, AppWidgetLocationFragment())
        }
    }

    private fun initViews() {
        binding.apply {
            backButton.setOnClickListener {
                setResult(RESULT_CANCELED)
                finish()
            }

            widgetStyleLight.setOnClickListener(::onWidgetStyleClick)
            widgetStyleDark.setOnClickListener(::onWidgetStyleClick)
            widgetStyleAdaptive.setOnClickListener(::onWidgetStyleClick)

            widgetStyleLightRadioButton.setOnCheckedChangeListener(::onRadioButtonCheckedChange)
            widgetStyleDarkRadioButton.setOnCheckedChangeListener(::onRadioButtonCheckedChange)
            widgetStyleAdaptiveRadioButton.setOnCheckedChangeListener(::onRadioButtonCheckedChange)

            widgetTransparencySeekBar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener {
                @SuppressLint("SetTextI18n")
                override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                    widgetBackgroundView.alpha = 1F - progress / 100F
                    widgetTransparencyValueTextView.text = "${progress}%"
                }

                override fun onStartTrackingTouch(seekBar: SeekBar) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar) {
                }
            })
            widgetTransparencyValueTextView.text = "0%"

            widgetConfigureCancelButton.setOnClickListener { onConfigurationCancel() }

            widgetConfigureSaveButton.setOnClickListener { onConfigurationSave() }
        }
    }

    private fun setupObserver() {
        supportFragmentManager.setFragmentResultListener(RequestKeys.SELECTED_LOCATION_WEATHER, this) { requestKey, result ->
            binding.apply {
                location = result
                    .getParcelable<Location>(ResultKeys.APP_WIDGET_LOCATION)
                    .let { requireNotNull(it) }

                presentWeather = result
                    .getParcelable<PresentWeather>(ResultKeys.APP_WIDGET_WEATHER)
                    .let { requireNotNull(it) }

                addressTextView.text = location.region3DepthName
                temperatureTextView.text = presentWeather.temperature.toString()

                weatherDescriptionTextView.text = presentWeather.description

                val iconResId = presentWeather.let {
                    val isNight = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
                    iconResIdOf(isNight, it.sky, it.precipitationType)
                }
                weatherIconImageView.setImageResource(iconResId)

                fineParticleGradeDescriptionTextView.text = presentWeather.fineParticleGrade.getDescription()
                val colorResId = presentWeather.fineParticleGrade.getColorResId()
                fineParticleGradeDescriptionTextView.setTextColor(
                    ContextCompat.getColor(this@AppWidgetConfigurationActivity, colorResId)
                )
            }
        }
    }

    private fun iconResIdOf(isNight: Boolean, sky: Sky, precipitationType: PrecipitationType): Int {
        return when (precipitationType) {
            PrecipitationType.NONE -> {
                when (sky) {
                    Sky.CLEAR -> {
                        if (isNight) R.drawable.icon_clear_night
                        else R.drawable.icon_clear
                    }
                    Sky.CLOUDY -> {
                        if (isNight) R.drawable.icon_cloudy_night
                        else R.drawable.icon_cloudy
                    }
                    Sky.OVERCAST -> R.drawable.icon_overcast
                }
            }
            PrecipitationType.RAIN -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_rain
                    Sky.CLOUDY -> {
                        if (isNight) R.drawable.icon_cloudy_rain_night
                        else R.drawable.icon_cloudy_rain
                    }
                    Sky.OVERCAST -> R.drawable.icon_overcast_rain
                }
            }
            PrecipitationType.SNOW -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_snow
                    Sky.CLOUDY -> {
                        if (isNight) R.drawable.icon_cloudy_snow_night
                        else R.drawable.icon_cloudy_snow
                    }
                    Sky.OVERCAST -> R.drawable.icon_overcast_snow
                }
            }
            PrecipitationType.RAIN_SNOW -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_overcast_rain_snow
                    Sky.CLOUDY -> {
                        if (isNight) R.drawable.icon_cloudy_rain_snow_night
                        else R.drawable.icon_cloudy_rain_snow
                    }
                    Sky.OVERCAST -> R.drawable.icon_overcast_rain_snow
                }
            }
            PrecipitationType.SHOWER -> {
                when (sky) {
                    Sky.CLEAR -> R.drawable.icon_overcast_shower
                    Sky.CLOUDY -> {
                        if (isNight) R.drawable.icon_cloudy_rain_snow_night
                        else R.drawable.icon_cloudy_rain_snow
                    }
                    Sky.OVERCAST -> R.drawable.icon_overcast_shower
                }
            }
        }
    }

    private fun onWidgetStyleClick(view: View) {
        binding.apply {
            widgetStyleLightRadioButton.isChecked = view == widgetStyleLight
            widgetStyleDarkRadioButton.isChecked = view == widgetStyleDark
            widgetStyleAdaptiveRadioButton.isChecked = view == widgetStyleAdaptive
        }
    }

    private fun onRadioButtonCheckedChange(button: CompoundButton, isChecked: Boolean) {
        if (!isChecked) {
            return
        }

        binding.apply {
            val backgroundTintColor: ColorStateList?
            val tintColor: Int
            val tintColorStateList: ColorStateList?
            val textColor: Int

            when (button) {
                widgetStyleLightRadioButton -> {
                    backgroundTintColor = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.white)
                    tintColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.black)
                    tintColorStateList = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.black)
                    textColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.black)
                }
                widgetStyleDarkRadioButton -> {
                    backgroundTintColor = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.black)
                    tintColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                    tintColorStateList = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.white)
                    textColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                }
                widgetStyleAdaptiveRadioButton -> {
                    val isNight = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

                    if (isNight) {
                        backgroundTintColor = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.black)
                        tintColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                        tintColorStateList = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.white)
                        textColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                    } else {
                        backgroundTintColor = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.black)
                        tintColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                        tintColorStateList = ContextCompat.getColorStateList(this@AppWidgetConfigurationActivity, R.color.white)
                        textColor = ContextCompat.getColor(this@AppWidgetConfigurationActivity, R.color.white)
                    }
                }
                else -> throw IllegalArgumentException()
            }

            widgetStyleLightRadioButton.isChecked = button == widgetStyleLightRadioButton
            widgetStyleDarkRadioButton.isChecked = button == widgetStyleDarkRadioButton
            widgetStyleAdaptiveRadioButton.isChecked = button == widgetStyleAdaptiveRadioButton

            widgetBackgroundView.backgroundTintList = backgroundTintColor

            addressTextView.setTextColor(textColor)
            currentLocationIconImageView.setColorFilter(tintColor)

            temperatureTextView.setTextColor(textColor)
            temperatureDegree.backgroundTintList = tintColorStateList

            weatherDescriptionTextView.setTextColor(textColor)
            fineParticleNameTextView.setTextColor(textColor)
        }
    }

    private fun onConfigurationCancel() {
        setResult(RESULT_CANCELED)
        finish()
    }

    private fun onConfigurationSave() {
        val uiMode = when {
            binding.widgetStyleLightRadioButton.isChecked -> AppWidgetUIMode.LIGHT
            binding.widgetStyleDarkRadioButton.isChecked -> AppWidgetUIMode.DARK
            else -> AppWidgetUIMode.ADAPTIVE
        }

        val transparency = binding.widgetTransparencySeekBar.progress

        val configure = AppWidgetConfigure(appWidgetID, location.id, uiMode, transparency)

        lifecycleScope.launch(Dispatchers.IO) {
            appWidgetDataManager.saveAppWidgetConfigure(configure)
        }

        val isNight = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        val weatherIconResId = iconResIdOf(isNight, presentWeather.sky, presentWeather.precipitationType)

        val appWidgetData = AppWidgetData(
            configure,
            isNight,
            location.region3DepthName,
            presentWeather.temperature,
            presentWeather.description,
            weatherIconResId,
            presentWeather.fineParticleGrade.getDescription(),
            presentWeather.fineParticleGrade.getColorResId()
        )

        updateAppWidget(
            this,
            AppWidgetManager.getInstance(this),
            appWidgetData
        )

        Intent()
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetID)
            .let { setResult(RESULT_OK, it) }

        finish()
    }
}