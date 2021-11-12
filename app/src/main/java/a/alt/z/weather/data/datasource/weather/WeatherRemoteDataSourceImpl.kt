package a.alt.z.weather.data.datasource.weather

import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetItem
import a.alt.z.weather.data.api.model.uvindex.UVIndexAreaCode
import a.alt.z.weather.data.api.model.weather.*
import a.alt.z.weather.data.api.model.uvindex.UVIndexItem
import a.alt.z.weather.data.api.service.*
import a.alt.z.weather.utils.projection.MapProjection
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import timber.log.Timber
import timber.log.debug
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val hourlyForecastService: HourlyForecastService,
    private val dailyForecastService: DailyForecastService,
    private val airQualityService: AirQualityService,
    private val livingWeatherIndexService: LivingWeatherIndexService,
    private val sunriseSunsetService: SunriseSunsetService
) : WeatherRemoteDataSource {

    private val zoneId = ZoneId.of("Asia/Seoul")
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm")
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
    private val timeFormatter = DateTimeFormatter.ofPattern("HHmm")

    override suspend fun getPresentWeather(latitude: Double, longitude: Double, regionDepth1Name: String): PresentWeatherItem {
        val now = LocalDateTime.now(zoneId)

        val (x, y) = MapProjection.transform(latitude, longitude)

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

        val presentAirQualityResponse = airQualityService.getPresentAirQuality(sidoName = regionDepth1Name.take(2))

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

    override suspend fun getHourlyWeather(latitude: Double, longitude: Double): List<HourlyWeatherItem> {
        val now = LocalDateTime.now(zoneId)

        val baseDateTime = if (now.hour < 20) {
            now.minusDays(1).withHour(20).withMinute(0)
        } else {
            now.withHour(2).withMinute(0)
        }

        val baseDate = dateFormatter.format(baseDateTime)

        val baseTime = timeFormatter.format(baseDateTime)

        val (x, y) = MapProjection.transform(latitude, longitude)

        val response = hourlyForecastService.getHourlyForecast3Days(
            baseDate = baseDate,
            baseTime = baseTime,
            x = x,
            y = y
        )

        return response.response.body.items.list
            .groupBy { LocalDateTime.parse(it.date + it.time, dateTimeFormatter) }
            .map { toHourlyWeatherItem(it.key, it.value) }
    }

    private fun toHourlyWeatherItem(dateTime: LocalDateTime, hourlyForecastItems: List<HourlyForecastItem>): HourlyWeatherItem {
        var probabilityOfPrecipitation = 0
        var precipitationCode = 0
        var precipitation = ""
        var humidity = 0
        var snow = 0F
        var skyCode = 1
        var temperature = 0
        var minTemperature = Int.MIN_VALUE
        var maxTemperature = Int.MAX_VALUE
        var windDirection = 0
        var windSpeed = 0F

        hourlyForecastItems.forEach { item ->
            when (item.category) {
                "POP" -> { probabilityOfPrecipitation = item.observedValue.toInt() }
                "PTY" -> { precipitationCode = item.observedValue.toInt() }
                "PCP" -> { precipitation = item.observedValue }
                "REH" -> { humidity = item.observedValue.toInt() }
                "SNO" -> {
                    /* TODO */
                    snow = if (item.observedValue == "적설없음") {
                        0F
                    } else {
                        item.observedValue.removeSuffix("cm").toFloat()
                    }
                }
                "SKY" -> { skyCode = item.observedValue.toInt() }
                "TMP" -> { temperature = item.observedValue.toInt() }
                "TMN" -> { minTemperature = item.observedValue.toFloat().toInt() }
                "TMX" -> { maxTemperature = item.observedValue.toFloat().toInt() }
                "VEC" -> { windDirection = item.observedValue.toInt() }
                "WSD" -> { windSpeed = item.observedValue.toFloat() }
            }
        }

        return HourlyWeatherItem(
            dateTime,
            temperature,
            skyCode,
            precipitationCode, precipitation, probabilityOfPrecipitation,
            snow,
            minTemperature, maxTemperature,
            humidity, windDirection, windSpeed
        )
    }

    override suspend fun getDailyWeather(regionDepth1Name: String, regionDepth2Name: String, regionDepth3Name: String): List<DailyWeatherItem> {
        val now = LocalDateTime.now(zoneId)

        val baseDateTime = when {
            now.hour >= 6 -> {
                DateTimeFormatter.ofPattern("yyyyMMdd").format(now) + "0600"
            }
            now.hour >= 18 -> {
                DateTimeFormatter.ofPattern("yyyyMMdd").format(now) + "1800"
            }
            else -> {
                DateTimeFormatter.ofPattern("yyyyMMdd").format(now.minusDays(1)) + "1800"
            }
        }

        val dailyForecastRegionId = DailyForecastRegionId.addressOf(regionDepth1Name)

        val dailyForecastResponse = dailyForecastService.getDailyForecast(regionId = dailyForecastRegionId.code, dateTime = baseDateTime)

        val dailyTemperatureRegionId = DailyTemperatureRegionId.addressOf(regionDepth1Name, regionDepth2Name, regionDepth3Name)

        val dailyTemperatureResponse = dailyForecastService.getDailyTemperature(regionId = dailyTemperatureRegionId.code, dateTime = baseDateTime)

        val dailyForecastItem = dailyForecastResponse.response.body.items.list.first()

        val dailyTemperatureItem = dailyTemperatureResponse.response.body.items.list.first()

        return dailyWeatherItemsOf(
            LocalDateTime.parse(baseDateTime, dateTimeFormatter),
            dailyForecastItem,
            dailyTemperatureItem
        )
    }

    private fun dailyWeatherItemsOf(
        baseDateTime: LocalDateTime,
        dailyForecastItem: DailyForecastItem,
        dailyTemperatureItem: DailyTemperatureItem
    ): List<DailyWeatherItem> {
        return listOf(
            DailyWeatherItem(
                baseDateTime.toLocalDate().plusDays(3),
                dailyForecastItem.pop3DaysAfterAm,
                dailyForecastItem.pop3DaysAfterPm,
                dailyForecastItem.forecast3DaysAfterAm,
                dailyForecastItem.forecast3DaysAfterPm,
                dailyTemperatureItem.minTemperature3DaysAfter,
                dailyTemperatureItem.maxTemperature3DaysAfter
            ),
            DailyWeatherItem(
                baseDateTime.toLocalDate().plusDays(4),
                dailyForecastItem.pop4DaysAfterAm,
                dailyForecastItem.pop4DaysAfterPm,
                dailyForecastItem.forecast4DaysAfterAm,
                dailyForecastItem.forecast4DaysAfterPm,
                dailyTemperatureItem.minTemperature4DaysAfter,
                dailyTemperatureItem.maxTemperature4DaysAfter
            ),
            DailyWeatherItem(
                baseDateTime.toLocalDate().plusDays(5),
                dailyForecastItem.pop5DaysAfterAm,
                dailyForecastItem.pop5DaysAfterPm,
                dailyForecastItem.forecast5DaysAfterAm,
                dailyForecastItem.forecast5DaysAfterPm,
                dailyTemperatureItem.minTemperature5DaysAfter,
                dailyTemperatureItem.maxTemperature5DaysAfter
            ),
            DailyWeatherItem(
                baseDateTime.toLocalDate().plusDays(6),
                dailyForecastItem.pop6DaysAfterAm,
                dailyForecastItem.pop6DaysAfterPm,
                dailyForecastItem.forecast6DaysAfterAm,
                dailyForecastItem.forecast6DaysAfterPm,
                dailyTemperatureItem.minTemperature6DaysAfter,
                dailyTemperatureItem.maxTemperature6DaysAfter
            ),
            DailyWeatherItem(
                baseDateTime.toLocalDate().plusDays(7),
                dailyForecastItem.pop7DaysAfterAm,
                dailyForecastItem.pop7DaysAfterPm,
                dailyForecastItem.forecast7DaysAfterAm,
                dailyForecastItem.forecast7DaysAfterPm,
                dailyTemperatureItem.minTemperature7DaysAfter,
                dailyTemperatureItem.maxTemperature7DaysAfter
            )
        )
    }

    override suspend fun getSunriseSunset(regionDepth1Name: String): SunriseSunsetItem {
        val dateString = dateFormatter.format(LocalDate.now())
        val area = regionDepth1Name.take(2)
        val sunriseSunsetResponse = sunriseSunsetService.getSunriseSunset(dateString, area)

        return sunriseSunsetResponse.body.items.item.first()
    }

    override suspend fun getUVIndex(regionDepth1Name: String): UVIndexItem {
        val now = LocalTime.now()

        val dateTime = if (now.hour > 6) {
            DateTimeFormatter.ofPattern("yyyyMMddHH").format(
                LocalDateTime.of(
                    LocalDate.now(),
                    LocalTime.of(6, 0)
                )
            )
        } else {
            DateTimeFormatter.ofPattern("yyyyMMddHH").format(
                LocalDateTime.of(
                    LocalDate.now().minusDays(1),
                    LocalTime.of(18, 0)
                )
            )
        }

        val uvIndexResponse = livingWeatherIndexService.getUVIndex(
            areaNo = UVIndexAreaCode.addressOf(regionDepth1Name).code,
            time = dateTime
        )

        return uvIndexResponse.response.body.items.list.first()
    }
}