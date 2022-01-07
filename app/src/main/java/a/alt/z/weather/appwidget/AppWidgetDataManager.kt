package a.alt.z.weather.appwidget

import a.alt.z.weather.R
import a.alt.z.weather.data.api.model.weather.PresentWeatherItem
import a.alt.z.weather.data.api.service.AirQualityService
import a.alt.z.weather.data.api.service.HourlyForecastService
import a.alt.z.weather.data.database.WeatherDatabase
import a.alt.z.weather.data.database.model.LocationEntity
import a.alt.z.weather.data.database.model.PresentWeatherEntity
import a.alt.z.weather.data.preferences.PreferencesStorage
import a.alt.z.weather.data.preferences.WeatherPreferencesStorage
import a.alt.z.weather.di.NetworkModule
import a.alt.z.weather.model.airquality.FineParticleGrade
import a.alt.z.weather.model.airquality.UltraFineParticleGrade
import a.alt.z.weather.model.appwidget.AppWidgetConfigure
import a.alt.z.weather.model.appwidget.AppWidgetData
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import a.alt.z.weather.model.weather.elements.SunriseSunset
import a.alt.z.weather.utils.projections.MapProjection
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter

class AppWidgetDataManager(context: Context) {

    private val database: WeatherDatabase = WeatherDatabase.getInstance(context)

    private val preferencesStorage: PreferencesStorage = WeatherPreferencesStorage(context)

    private val hourlyForecastService: HourlyForecastService by lazy {
        val interceptor = NetworkModule.providesInterceptor()
        val client = NetworkModule.providesClient(interceptor)
        val converterFactory = NetworkModule.providesConverterFactory()
        NetworkModule.providesHourlyForecastService(client, converterFactory)
    }

    private val airQualityService: AirQualityService by lazy {
        val interceptor = NetworkModule.providesInterceptor()
        val client = NetworkModule.providesClient(interceptor)
        val converterFactory = NetworkModule.providesConverterFactory()
        NetworkModule.providesAirQualityService(client, converterFactory)
    }

    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")

    private val timeFormatter = DateTimeFormatter.ofPattern("HHmm")

    suspend fun getLocations(): List<Location> {
        return database.locationDao()
            .getLocationsSnapshot()
            .map(::transform)
    }

    private fun transform(location: LocationEntity) = with(location) {
        Location(
            id,
            isDeviceLocation,
            latitude, longitude,
            address,
            region1DepthName, region2DepthName, region3DepthName)
    }

    suspend fun applyNightMode() {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        val sunriseSunsetEntity = withContext(Dispatchers.IO) {
            database.sunriseSunsetDao().getSunriseSunset(now.toLocalDate())
        }

        val sunriseSunset = sunriseSunsetEntity
            ?.let {
                SunriseSunset(
                    LocalDateTime.of(now.toLocalDate(), LocalTime.from(timeFormatter.parse(it.sunrise))),
                    LocalDateTime.of(now.toLocalDate(), LocalTime.from(timeFormatter.parse(it.sunset)))
                )
            }
            ?: getDefaultSunriseSunset(now.toLocalDate())

        if (now in sunriseSunset.sunrise..sunriseSunset.sunset) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
    }

    private fun getDefaultSunriseSunset(today: LocalDate): SunriseSunset {
        return when (today.month) {
            Month.JANUARY -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(7, 45)),
                LocalDateTime.of(today, LocalTime.of(17, 30))
            )
            Month.FEBRUARY -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(7, 28)),
                LocalDateTime.of(today, LocalTime.of(18, 3))
            )
            Month.MARCH -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(6, 53)),
                LocalDateTime.of(today, LocalTime.of(18, 32))
            )
            Month.APRIL -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(6, 8)),
                LocalDateTime.of(today, LocalTime.of(18, 32))
            )
            Month.MAY -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(5, 30)),
                LocalDateTime.of(today, LocalTime.of(19, 27))
            )
            Month.JUNE -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(5, 12)),
                LocalDateTime.of(today, LocalTime.of(19, 51))
            )
            Month.JULY -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(5, 19)),
                LocalDateTime.of(today, LocalTime.of(19, 55))
            )
            Month.AUGUST -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(5, 41)),
                LocalDateTime.of(today, LocalTime.of(19, 33))
            )
            Month.SEPTEMBER -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(6, 7)),
                LocalDateTime.of(today, LocalTime.of(18, 50))
            )
            Month.OCTOBER -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(6, 33)),
                LocalDateTime.of(today, LocalTime.of(18, 5))
            )
            Month.NOVEMBER -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(7, 4)),
                LocalDateTime.of(today, LocalTime.of(17, 28))
            )
            Month.DECEMBER -> SunriseSunset(
                LocalDateTime.of(today, LocalTime.of(7, 33)),
                LocalDateTime.of(today, LocalTime.of(17, 15))
            )
            else -> throw IllegalArgumentException()
        }
    }

    suspend fun getPresentWeather(location: Location): PresentWeather {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        val baseDateTime = if (now.minute > 40) {
            now.withMinute(0).withSecond(0).withNano(0)
        } else {
            now.minusHours(1).withMinute(0).withSecond(0).withNano(0)
        }

        val presentWeatherEntity = database.weatherDao().getPresentWeather(location.id, baseDateTime)

        return if (presentWeatherEntity == null) {
            fetchPresentWeather(location)
                .transform(location)
                .also { database.weatherDao().insertPresentWeather(it) }
                .transform()
        } else {
            presentWeatherEntity.transform()
        }
    }

    private fun PresentWeatherItem.transform(location: Location) = PresentWeatherEntity(
        0L,
        location.id,
        skyCode,
        temperature,
        precipitationTypeCode, precipitation,
        windSpeed, windDirection, humidity,
        fineParticleValue, ultraFineParticleValue,
        baseDateTime
    )

    private fun PresentWeatherEntity.transform() = PresentWeather(
        Sky.codeOf(skyCode),
        temperature,
        PrecipitationType.codeOf(precipitationTypeCode),
        precipitation,
        windSpeed,
        windDirection,
        humidity,
        FineParticleGrade.levelOf(fineParticleValue),
        fineParticleValue,
        UltraFineParticleGrade.levelOf(ultraFineParticleValue),
        ultraFineParticleValue
    )

    private suspend fun fetchPresentWeather(location: Location): PresentWeatherItem {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        val (x, y) = MapProjection.transform(location.latitude, location.longitude)

        val baseDateTimeOfHourlyForecast6Hours = now.minusHours(1).withMinute(30)

        val hourlyForecast6HoursResponse = hourlyForecastService.getHourlyForecast6Hours(
            baseDate = dateFormatter.format(baseDateTimeOfHourlyForecast6Hours),
            baseTime = timeFormatter.format(baseDateTimeOfHourlyForecast6Hours),
            x = x,
            y = y
        )

        val baseDateTimeOfNowcast = if (now.minute > 40) {
            now.withMinute(0).withSecond(0).withNano(0)
        } else {
            now.minusHours(1).withMinute(0).withSecond(0).withNano(0)
        }

        val nowcastResponse = hourlyForecastService.getNowcast(
            baseDate = dateFormatter.format(baseDateTimeOfNowcast),
            baseTime = timeFormatter.format(baseDateTimeOfNowcast),
            x = x,
            y = y
        )

        val presentAirQualityResponse = airQualityService.getPresentAirQuality(sidoName = location.region1DepthName.take(2))

        var sky = 1
        var temperature = 0
        var precipitation1Hour = 0F
        var precipitationType = 0
        var humidity = 0
        var windDirection = 0
        var windSpeed = 0F
        var pm10Value = 0
        var pm25Value = 0

        val dateTime = dateTimeFormatter.format(now.withMinute(0))

        hourlyForecast6HoursResponse.response.body.items.list
            .find { it.date + it.time == dateTime && it.category == "SKY" }
            ?.let { sky = it.observedValue.toInt() }

        nowcastResponse.response.body.items.list.forEach {
            when (it.category) {
                "T1H" -> {
                    temperature = it.observedValue.toFloat().toInt()
                }
                "RN1" -> {
                    precipitation1Hour = it.observedValue.toFloat()
                }
                "PTY" -> {
                    precipitationType = it.observedValue.toInt()
                }
                "REH" -> {
                    humidity = it.observedValue.toInt()
                }
                "WSD" -> {
                    windSpeed = it.observedValue.toFloat()
                }
                "VEC" -> {
                    windDirection = it.observedValue.toInt()
                }
            }
        }

        presentAirQualityResponse.response.body.items.find {
            val fineParticleLevel = it.pm10Value?.toIntOrNull()
            val ultraFineParticleLevel = it.pm25Value?.toIntOrNull()

            if (fineParticleLevel != null && ultraFineParticleLevel != null) {
                pm10Value = fineParticleLevel
                pm25Value = ultraFineParticleLevel
                true
            } else {
                false
            }
        }

        return PresentWeatherItem(
            baseDateTimeOfNowcast,
            sky,
            temperature,
            precipitationType,
            precipitation1Hour,
            windSpeed,
            windDirection,
            humidity,
            pm10Value,
            pm25Value
        )
    }

    suspend fun getAppWidgetData(widgetID: Int): AppWidgetData? {
        val configure = preferencesStorage.appWidgetConfigures.find { it.widgetID == widgetID } ?: return null

        val location = withContext(Dispatchers.IO) {
            database.locationDao()
                .getLocation(configure.locationID)
                .let(::transform)
        }

        val presentWeather = withContext(Dispatchers.IO) {
            getPresentWeather(location)
        }

        applyNightMode()

        val isNight = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES

        val weatherIconResId = iconResIdOf(isNight, presentWeather.sky, presentWeather.precipitationType)

        return AppWidgetData(
            configure,
            isNight,
            location.region3DepthName,
            presentWeather.temperature,
            presentWeather.description,
            weatherIconResId,
            presentWeather.fineParticleGrade.getDescription(),
            presentWeather.fineParticleGrade.getColorResId()
        )
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

    fun saveAppWidgetConfigure(configure: AppWidgetConfigure) {
        preferencesStorage.appWidgetConfigures
            .toMutableList()
            .apply { add(configure) }
            .let { preferencesStorage.appWidgetConfigures = it }
    }

    fun removeAppWidgetConfigure(appWidgetID: Int) {
        preferencesStorage.appWidgetConfigures
            .toMutableList()
            .filter { it.widgetID == appWidgetID }
            .let { preferencesStorage.appWidgetConfigures = it }
    }

    fun clearAppWidgetConfigures() {
        preferencesStorage.appWidgetConfigures = emptyList()
    }

    companion object {
        @Volatile private var appWidgetDataManager: AppWidgetDataManager? = null

        fun getInstance(context: Context) = appWidgetDataManager ?: synchronized(this) {
            AppWidgetDataManager(context).also {
                appWidgetDataManager = it
            }
        }
    }
}