package a.alt.z.weather.data.datasource

import a.alt.z.weather.data.api.model.mid.MidLandForecastItem
import a.alt.z.weather.data.api.model.mid.MidTemperatureItem
import a.alt.z.weather.data.api.model.srt.ForecastItem

interface WeatherRemoteDataSource {

    suspend fun getForecast(baseDate: String,
                            baseTime: String,
                            x: Int,
                            y: Int): List<ForecastItem>

    suspend fun getMidLandForecast(regionId: String, dateTime: String): List<MidLandForecastItem>

    suspend fun getMidTemperature(regionId: String, dateTime: String): List<MidTemperatureItem>
}