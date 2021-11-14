package a.alt.z.weather.service

import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetItem
import a.alt.z.weather.data.api.model.uvindex.UVIndexItem
import a.alt.z.weather.data.api.model.weather.DailyWeatherItem
import a.alt.z.weather.data.api.model.weather.HourlyWeatherItem
import a.alt.z.weather.data.database.dao.LocationDao
import a.alt.z.weather.data.database.model.*
import a.alt.z.weather.data.datasource.weather.WeatherLocalDataSource
import a.alt.z.weather.data.datasource.weather.WeatherRemoteDataSource
import a.alt.z.weather.domain.usecase.location.GetLocationsUseCase
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.elements.PrecipitationType
import a.alt.z.weather.model.weather.elements.Sky
import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import timber.log.debug

@HiltWorker
class SyncWeatherDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val locationDao: LocationDao,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        Timber.debug { "SyncWorker::doWork" }

        try {

        } catch (exception: Exception) {

        }
        locationDao
            .getLocationsSnapshot()
            .let { Timber.debug { "locations::$it" } }

        return Result.success()
    }

    private suspend fun syncWeatherData(location: Location) {
        Timber.debug { "<syncWeatherData::location(${location})>" }
        /* 1. HOURLY WEATHER */
        val hourlyWeatherItems = weatherRemoteDataSource.getHourlyWeather(location.latitude, location.longitude)
        val hourlyWeatherEntities = hourlyWeatherItems.map { it.transform(location) }.also { Timber.debug { "HOURLY WEATHER::updated ${it.size} hWeathers" } }
        weatherLocalDataSource.saveHourlyWeathers(hourlyWeatherEntities)

        /* 2. DAILY WEATHER */
        val dailyWeatherEntities = weatherLocalDataSource.getDailyWeathers(location.id)
        val after3Days = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(3)
        if (dailyWeatherEntities.count { it.date < after3Days } < 3) {
            hourlyWeatherItems
                .filter { it.minTemperature != Int.MIN_VALUE || it.maxTemperature != Int.MAX_VALUE }
                .groupBy { it.dateTime.toLocalDate() }
                .map { entry ->
                    val beforeNoonWeather = entry.value.find { it.minTemperature != Int.MIN_VALUE }
                    val afternoonWeather = entry.value.find { it.maxTemperature != Int.MAX_VALUE }

                    requireNotNull(beforeNoonWeather)
                    requireNotNull(afternoonWeather)

                    DailyWeatherEntity(
                        0L,
                        location.id,
                        entry.key,
                        beforeNoonWeather.minTemperature, afternoonWeather.maxTemperature,
                        beforeNoonWeather.skyCode, afternoonWeather.skyCode,
                        beforeNoonWeather.probabilityOfPrecipitation, afternoonWeather.probabilityOfPrecipitation,
                        beforeNoonWeather.precipitationCode, afternoonWeather.precipitationCode
                    )
                }
                .let { weatherLocalDataSource.saveDailyWeathers(it) }
        }

        val dailyWeatherItems = weatherRemoteDataSource.getDailyWeather(
            location.region1DepthName,
            location.region2DepthName,
            location.region3DepthName
        )

        dailyWeatherItems
            .map { it.transform(location) }
            .also { Timber.debug { "DAILY WEATHER::updated $it" } }
            .let { weatherLocalDataSource.saveDailyWeathers(it) }

        /* SUNRISE + SUNSET */
        weatherRemoteDataSource.getSunriseSunset(location.region1DepthName)
            .transform(location)
            .also { Timber.debug { "SUNRISE + SUNSET::updated $it" } }
            .let { weatherLocalDataSource.saveSunriseSunset(it) }

        /* UV INDEX */
        weatherRemoteDataSource.getUVIndex(location.region1DepthName)
            .transform(location)
            .also { Timber.debug { "UV INDEX::updated $it" } }
            .let { weatherLocalDataSource.saveUVIndices(it) }

        Timber.debug { "</syncWeatherData>" }
    }

    private fun LocationEntity.transform() = Location(
        id,
        isDeviceLocation,
        latitude, longitude,
        address,
        region1DepthName, region2DepthName, region3DepthName
    )

    private fun HourlyWeatherItem.transform(location: Location) = HourlyWeatherEntity(
        0L,
        location.id,
        dateTime,
        temperature, minTemperature, maxTemperature,
        skyCode,
        probabilityOfPrecipitation, precipitationCode, precipitationValueOf(precipitation), precipitationOrdinalOf(precipitation),
        snow,
        humidity, windDirection, windSpeed,
        LocalDate.now()
    )

    private fun precipitationValueOf(precipitationString: String): Int {
        return try {
            precipitationString.removeSuffix("mm").toInt()
        } catch (exception: Exception) {
            0
        }
    }

    private fun precipitationOrdinalOf(precipitationString: String): Int {
        return when (precipitationString) {
            "강수없음" -> { 0 }
            "1mm 미만" -> { 1 }
            "30~50mm" -> { 3 }
            "50mm 이상" -> { 4 }
            else -> { 2 }
        }
    }

    private fun DailyWeatherItem.transform(location: Location) = DailyWeatherEntity(
        0L,
        location.id,
        date,
        minTemperature, maxTemperature,
        skyOf(forecastBeforeNoon).code, skyOf(forecastAfterNoon).code,
        precipitationTypeOf(forecastBeforeNoon).code, precipitationTypeOf(forecastAfterNoon).code,
        popBeforeNoon, popAfternoon
    )

    private fun skyOf(forecast: String): Sky {
        return when {
            forecast == "맑음" -> Sky.CLEAR
            forecast == "구름많음" || forecast.startsWith("구름많고") -> Sky.CLOUDY
            forecast == "흐림" || forecast.startsWith("흐리고") -> Sky.OVERCAST
            else -> throw IllegalArgumentException()
        }
    }

    private fun precipitationTypeOf(forecast: String): PrecipitationType {
        return when {
            forecast.endsWith("비") -> PrecipitationType.RAIN
            forecast.endsWith("눈") -> PrecipitationType.SNOW
            forecast.endsWith("비/눈") -> PrecipitationType.RAIN_SNOW
            forecast.endsWith("소나기") -> PrecipitationType.SHOWER
            else -> PrecipitationType.NONE
        }
    }

    private fun SunriseSunsetItem.transform(location: Location) = SunriseSunsetEntity(
        0L,
        location.id,
        LocalDate.now(),
        requireNotNull(sunrise).trim(),
        requireNotNull(sunset).trim()
    )

    private fun UVIndexItem.transform(location: Location): List<UVIndexEntity> {
        val today = LocalDate.now(ZoneId.of("Asia/Seoul"))

        val baseDateTime = DateTimeFormatter.ofPattern("yyyyMMddHH")
            .parse(date)
            .let { LocalDateTime.from(it) }

        return if (today.dayOfMonth == baseDateTime.dayOfMonth) {
            listOf(
                UVIndexEntity(0L, location.id, today, this.today.toInt()),
                UVIndexEntity(0L, location.id, today.plusDays(1), tomorrow.toInt()),
                UVIndexEntity(0L, location.id, today.plusDays(2), dayAfterTomorrow.toInt())
            )
        } else {
            listOf(
                UVIndexEntity(0L, location.id, today, tomorrow.toInt()),
                UVIndexEntity(0L, location.id, today.plusDays(1), dayAfterTomorrow.toInt()),
                UVIndexEntity(0L, location.id, today.plusDays(2), twoDaysAfterTomorrow.toInt())
            )
        }
    }
}