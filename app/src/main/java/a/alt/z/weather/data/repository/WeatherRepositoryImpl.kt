package a.alt.z.weather.data.repository

import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetItem
import a.alt.z.weather.data.api.model.uvindex.UVIndexItem
import a.alt.z.weather.data.api.model.weather.DailyWeatherItem
import a.alt.z.weather.data.api.model.weather.HourlyWeatherItem
import a.alt.z.weather.data.api.model.weather.PresentWeatherItem
import a.alt.z.weather.data.database.model.*
import a.alt.z.weather.data.datasource.weather.WeatherLocalDataSource
import a.alt.z.weather.data.datasource.weather.WeatherRemoteDataSource
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.model.airquality.FineParticleGrade
import a.alt.z.weather.model.airquality.UltraFineParticleGrade
import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.location.PresentWeatherByLocation
import a.alt.z.weather.model.location.PreviewPresentWeather
import a.alt.z.weather.model.weather.*
import a.alt.z.weather.model.weather.elements.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
): WeatherRepository {

    override suspend fun getPresentWeather(location: Location): PresentWeather {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))

        val baseDateTime = if (now.minute > 40) {
            now.withMinute(0).withSecond(0).withNano(0)
        } else {
            now.minusHours(1).withMinute(0).withSecond(0).withNano(0)
        }

        val presentWeatherEntity = weatherLocalDataSource.getPresentWeather(location.id, baseDateTime)

        return if (presentWeatherEntity == null) {
            weatherRemoteDataSource
                .getPresentWeather(location.latitude, location.longitude, location.region1DepthName)
                .transform(location)
                .also { weatherLocalDataSource.savePresentWeather(it) }
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

    override suspend fun getPreviewPresentWeather(address: Address): PreviewPresentWeather {
        return weatherRemoteDataSource
            .getPresentWeather(address.latitude, address.longitude, address.region1DepthName)
            .transform()
            .let { weather -> PreviewPresentWeather(address, weather) }
    }

    private fun PresentWeatherItem.transform() = PresentWeather(
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

    override suspend fun getForecastWeather(location: Location): ForecastWeather {
        val hourlyWeatherEntities = weatherLocalDataSource.getHourlyWeathers(location.id)
        val dailyWeatherEntities = weatherLocalDataSource.getDailyWeathers(location.id)
        val sunriseSunsetEntity = weatherLocalDataSource.getSunriseSunset()
        val uvIndexEntity = weatherLocalDataSource.getUVIndex(location.id)

        val hourlyWeathers = if (hourlyWeatherEntities.isEmpty()) {
            /* HOURLY WEATHER */
            val hourlyWeatherItems = weatherRemoteDataSource.getHourlyWeather(location.latitude, location.longitude)

            val hourlyWeathers = hourlyWeatherItems
                .map { it.transform(location) }
                .also { weatherLocalDataSource.saveHourlyWeathers(it) }
                .map { it.transform() }

            /* DAILY WEATHER */
            if (dailyWeatherEntities.size < 7 &&
                dailyWeatherEntities.count { it.date in LocalDate.now()..LocalDate.now().plusDays(2) } < 3) {
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
                .also { weatherLocalDataSource.saveDailyWeathers(it) }

            hourlyWeathers
        } else {
            hourlyWeatherEntities.map { it.transform() }
        }

        val dailyWeathers = weatherLocalDataSource.getDailyWeathers(location.id).map { it.transform() }

        val sunriseSunset = if (sunriseSunsetEntity == null) {
            val sunriseSunsetItem = weatherRemoteDataSource.getSunriseSunset(location.region1DepthName)

            sunriseSunsetItem
                .transform(location)
                .also { weatherLocalDataSource.saveSunriseSunset(it) }
                .let { it.transform() }
        } else {
            sunriseSunsetEntity.transform()
        }

        val uvIndex = if (uvIndexEntity == null) {
            val uvIndexItem = weatherRemoteDataSource.getUVIndex(location.region1DepthName)

            uvIndexItem
                .transform(location)
                .also { weatherLocalDataSource.saveUVIndices(it) }
                .first()
                .transform()
        } else {
            uvIndexEntity.transform()
        }

        return ForecastWeather(hourlyWeathers, dailyWeathers, uvIndex, sunriseSunset)
    }

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

    private fun HourlyWeatherEntity.transform() = HourlyWeather(
        dateTime,
        temperature,
        Sky.codeOf(skyCode),
        probabilityOfPrecipitation, PrecipitationType.codeOf(precipitationCode), Precipitation.values()[precipitationOrdinal], precipitation,
        snow,
        humidity, windDirection, windSpeed
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

    private fun DailyWeatherItem.transform() = DailyWeather(
        date,
        minTemperature, maxTemperature,
        skyOf(forecastBeforeNoon), skyOf(forecastAfterNoon),
        precipitationTypeOf(forecastBeforeNoon), precipitationTypeOf(forecastAfterNoon),
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

    private fun DailyWeatherEntity.transform() = DailyWeather(
        date,
        minTemperature,
        maxTemperature,
        Sky.codeOf(skyCodeBeforeNoon),
        Sky.codeOf(skyCodeAfternoon),
        PrecipitationType.codeOf(precipitationCodeBeforeNoon),
        PrecipitationType.codeOf(precipitationCodeAfternoon),
        probabilityOfPrecipitationBeforeNoon,
        probabilityOfPrecipitationAfternoon
    )

    private fun SunriseSunsetItem.transform(location: Location) = SunriseSunsetEntity(
        0L,
        location.id,
        LocalDate.now(),
        requireNotNull(sunrise).trim(),
        requireNotNull(sunset).trim()
    )

    private fun SunriseSunsetEntity.transform(): SunriseSunset {
        val timeFormatter = DateTimeFormatter.ofPattern("HHmm")

        return SunriseSunset(
            LocalDateTime.of(
                LocalDate.now(),
                LocalTime.from(timeFormatter.parse(sunrise))
            ),
            LocalDateTime.of(
                LocalDate.now(),
                LocalTime.from(timeFormatter.parse(sunset))
            )
        )
    }

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

    private fun UVIndexEntity.transform() = UVIndex(index)

    override suspend fun getPresentWeathersByLocations(locations: List<Location>): List<PresentWeatherByLocation> {
        return locations.map { location ->
            PresentWeatherByLocation(getPresentWeather(location), location)
        }
    }
}