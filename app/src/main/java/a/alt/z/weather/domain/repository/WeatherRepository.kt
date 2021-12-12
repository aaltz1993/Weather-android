package a.alt.z.weather.domain.repository

import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.location.PresentWeatherByLocation
import a.alt.z.weather.model.location.PreviewPresentWeather
import a.alt.z.weather.model.weather.ForecastWeather
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.weather.elements.SunriseSunset

interface WeatherRepository {

    suspend fun getSunriseSunset(location: Location): SunriseSunset

    suspend fun getPresentWeather(location: Location): PresentWeather

    suspend fun getPresentWeatherBackward(location: Location): PresentWeather

    suspend fun getForecastWeather(location: Location): ForecastWeather

    suspend fun getPreviewPresentWeather(address: Address): PreviewPresentWeather

    suspend fun getPresentWeathersByLocations(locations: List<Location>): List<PresentWeatherByLocation>

    suspend fun deleteWeatherData(locationId: Long)
}