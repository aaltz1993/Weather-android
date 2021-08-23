package a.alt.z.weather.data.datasource

import a.alt.z.weather.data.api.MidForecastService
import a.alt.z.weather.data.api.ShortForecastService
import a.alt.z.weather.data.api.model.mid.MidLandForecastItem
import a.alt.z.weather.data.api.model.mid.MidTemperatureItem
import a.alt.z.weather.data.api.model.srt.ForecastItem
import retrofit2.http.Query
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val shortForecastService: ShortForecastService,
    private val midForecastService: MidForecastService
) : WeatherRemoteDataSource {

    override suspend fun getForecast(baseDate: String, baseTime: String, x: Int, y: Int): List<ForecastItem> {
        return shortForecastService.getForecast(baseDate = baseDate, baseTime = baseTime, x = x, y = y)
            .response
            .body
            .items
            .list
    }

    override suspend fun getMidLandForecast(regionId: String, dateTime: String): List<MidLandForecastItem> {
        return midForecastService.getMidLandForecast(regionId = regionId, dateTime = dateTime)
            .response
            .body
            .items
            .list
    }

    override suspend fun getMidTemperature(regionId: String, dateTime: String): List<MidTemperatureItem> {
        return midForecastService.getMidTemperature(regionId = regionId, dateTime = dateTime)
            .response
            .body
            .items
            .list
    }
}