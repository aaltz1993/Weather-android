package a.alt.z.weather.data.datasource.weather

import a.alt.z.weather.model.location.Location
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import javax.inject.Inject

class WeatherDataSourceImpl @Inject constructor(
    private val localDataSource: WeatherLocalDataSource,
    private val remoteDataSource: WeatherRemoteDataSource
) {

    /**
     * 현재 시간 ~ 업데이트된 시간
     * 업데이트 주기보다
     *  길면 업데이트
     *  짧으면
     */
    suspend fun fetchWeatherForecast(location: Location, updatedAt: LocalDate) {
        val now = LocalDateTime.now(ZoneId.of("Asia/Seoul"))


        /*
        hourlyWeathers
            .map {
                HourlyWeather(
                    it.dateTime,
                    it.temperature,
                    Sky.codeOf(it.skyCode),
                    PrecipitationType.codeOf(it.precipitationCode),
                    it.probabilityOfPrecipitation
                )
            }.forEach {
                Timber.debug { "$it" }
            }*/

        /*
        remoteDataSource.getForecastWeather(x, y)
            .groupBy { it.date + it.time }
            .map { entry ->
                /*
                TMP UUU VVV VEC WSD SKY PTY POP PCP REH SNO
                TMN TMX WAV
                 */
                var probabilityOfPrecipitation = 0
                var precipitationCode = 0
                var precipitation = 0
                var humidity = 0
                var snow = 0
                var skyCode = 1
                var temperature = 0

                var minTemperature = 0F
                var maxTemperature = 0F

                entry.value.forEach {
                    when (it.category) {
                        "POP" -> {
                            probabilityOfPrecipitation = it.observedValue.toInt()
                        }
                        "PTY" -> {
                            precipitationCode = it.observedValue.toInt()
                        }
                        "PCP" -> {
                            precipitation = it.observedValue.toIntOrNull() ?: 0
                        }
                        "REH" -> {
                            humidity = it.observedValue.toInt()
                        }
                        "SNO" -> {
                            snow = it.observedValue.toIntOrNull() ?: 0
                        }
                        "SKY" -> {
                            skyCode = it.observedValue.toInt()
                        }
                        "TMP" -> {
                            temperature = it.observedValue.toInt()
                        }
                        "TMN" -> {
                            minTemperature = it.observedValue.toFloat()
                        }
                        "TMX" -> {
                            maxTemperature = it.observedValue.toFloat()
                        }
                    }
                }

                val dateTime = LocalDateTime.from(DateTimeFormatter.ofPattern("yyyyMMddHHmm").parse(entry.key))

                HourlyWeatherEntity(
                    dateTime,
                    probabilityOfPrecipitation,
                    precipitationCode,
                    precipitation,
                    humidity,
                    snow,
                    skyCode,
                    temperature,
                    x,
                    y,
                    now
                )
            }
            .let { localDataSource.saveHourlyWeathers(it) }
         */
    }
}