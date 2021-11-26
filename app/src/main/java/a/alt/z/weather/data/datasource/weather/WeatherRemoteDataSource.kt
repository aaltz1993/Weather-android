package a.alt.z.weather.data.datasource.weather

import a.alt.z.weather.data.api.model.airquality.AirQualityItem
import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetItem
import a.alt.z.weather.data.api.model.weather.*
import a.alt.z.weather.data.api.model.uvindex.UVIndexItem

interface WeatherRemoteDataSource {

    suspend fun getPresentWeather(latitude: Double, longitude: Double, regionDepth1Name: String): PresentWeatherItem

    suspend fun getPresentAirQuality(regionDepth1Name: String): AirQualityItem

    suspend fun getHourlyWeather(latitude: Double, longitude: Double): List<HourlyWeatherItem>

    suspend fun getDailyWeather(regionDepth1Name: String, regionDepth2Name: String, regionDepth3Name: String): List<DailyWeatherItem>

    suspend fun getSunriseSunset(regionDepth1Name: String): SunriseSunsetItem

    suspend fun getUVIndex(regionDepth1Name: String): UVIndexItem
}