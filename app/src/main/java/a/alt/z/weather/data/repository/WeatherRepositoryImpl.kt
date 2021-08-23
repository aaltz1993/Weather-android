package a.alt.z.weather.data.repository

import a.alt.z.weather.data.datasource.WeatherLocalDataSource
import a.alt.z.weather.data.datasource.WeatherRemoteDataSource
import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.model.HourlyForecast
import a.alt.z.weather.model.PrecipitationType
import a.alt.z.weather.model.Sky
import a.alt.z.weather.model.WeeklyForecast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val weatherLocalDataSource: WeatherLocalDataSource,
    private val weatherRemoteDataSource: WeatherRemoteDataSource
): WeatherRepository {

    override suspend fun getHourlyForecast(baseDate: String, baseTime: String, x: Int, y: Int): List<HourlyForecast> {
        val forecastItems = withContext(Dispatchers.IO) {
            weatherRemoteDataSource.getForecast(baseDate, baseTime, x, y)
        }

        val minTemperature = forecastItems.find { it.category == "TMN" }?.observedValue?.toInt() ?: 0
        val maxTemperature = forecastItems.find { it.category == "TMX" }?.observedValue?.toInt() ?: 0

        return withContext(Dispatchers.Default) {
            forecastItems
                .groupBy { it.date + it.time }
                .map {
                    val items = it.value
                    val date = items.firstOrNull()?.date.orEmpty()
                    val time = items.firstOrNull()?.time.orEmpty()
                    val temperature = items.find { item -> item.category == "TMP" }?.observedValue?.toInt() ?: 0
                    val probabilityOfPrecipitation = items.find { item -> item.category == "POP" }?.observedValue?.toInt() ?: 0
                    val skyCode = items.find { item -> item.category == "SKY" }?.observedValue?.toInt() ?: 0
                    val sky = Sky.valueOf(skyCode)
                    val ptyCode = items.find { item -> item.category == "PTY" }?.observedValue?.toInt() ?: 0
                    val precipitationType = PrecipitationType.valueOf(ptyCode)

                    HourlyForecast(date, time, temperature, probabilityOfPrecipitation, sky, precipitationType)
                }
        }
    }

    override suspend fun getWeeklyForecast(baseDate: String, baseTime: String, x: Int, y: Int): List<WeeklyForecast> {
        TODO("Not yet implemented")
    }
}