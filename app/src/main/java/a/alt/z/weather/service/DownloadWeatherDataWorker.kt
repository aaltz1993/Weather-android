package a.alt.z.weather.service

import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetItem
import a.alt.z.weather.data.api.model.uvindex.UVIndexItem
import a.alt.z.weather.data.api.model.weather.DailyWeatherItem
import a.alt.z.weather.data.api.model.weather.HourlyWeatherItem
import a.alt.z.weather.data.database.dao.LocationDao
import a.alt.z.weather.data.database.model.*
import a.alt.z.weather.data.datasource.weather.WeatherLocalDataSource
import a.alt.z.weather.data.datasource.weather.WeatherRemoteDataSource
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

@HiltWorker
class DownloadWeatherDataWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val locationDao: LocationDao,
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherLocalDataSource
): CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            locationDao.getLocationsSnapshot()
                .map { it.transform() }
                .forEach { location -> downloadWeatherData(location) }

            Result.success()
        } catch (exception: Exception) {
            Result.retry()
        }
    }

    private suspend fun downloadWeatherData(location: Location) {
        /* 1. HOURLY WEATHER */
        val hourlyWeatherItems = weatherRemoteDataSource.getHourlyWeather(location.latitude, location.longitude)
        val hourlyWeatherEntities = hourlyWeatherItems.map { it.transform(location) }
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
            .let { weatherLocalDataSource.saveDailyWeathers(it) }

        /* SUNRISE + SUNSET */
        weatherRemoteDataSource.getSunriseSunset(location.latitude, location.longitude)
            .transform(location)
            .let { weatherLocalDataSource.saveSunriseSunset(it) }

        /* UV INDEX */
        weatherRemoteDataSource.getUVIndex(location.region1DepthName)
            .transform(location)
            .let { weatherLocalDataSource.saveUVIndices(it) }
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
        snowValueOf(snow), snowOrdinalOf(snow),
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
            "????????????" -> { 0 }
            "1mm ??????" -> { 1 }
            "30~50mm" -> { 3 }
            "50mm ??????" -> { 4 }
            else -> { 2 }
        }
    }

    private fun snowValueOf(snowString: String): Float {
        return try {
            snowString.removeSuffix("cm").toFloat()
        } catch (exception: Exception) {
            0F
        }
    }

    private fun snowOrdinalOf(snowString: String): Int {
        return when (snowString) {
            "????????????" -> 0
            "1cm??????" -> 1
            "5cm??????", "5cm ??????" -> 3
            else -> 2
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
            forecast == "??????" -> Sky.CLEAR
            forecast == "????????????" || forecast.startsWith("????????????") -> Sky.CLOUDY
            forecast == "??????" || forecast.startsWith("?????????") -> Sky.OVERCAST
            else -> throw IllegalArgumentException()
        }
    }

    private fun precipitationTypeOf(forecast: String): PrecipitationType {
        return when {
            forecast.endsWith("???") -> PrecipitationType.RAIN
            forecast.endsWith("???") -> PrecipitationType.SNOW
            forecast.endsWith("???/???") -> PrecipitationType.RAIN_SNOW
            forecast.endsWith("?????????") -> PrecipitationType.SHOWER
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