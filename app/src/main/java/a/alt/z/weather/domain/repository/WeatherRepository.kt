package a.alt.z.weather.domain.repository

import a.alt.z.weather.model.location.Address
import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.ForecastWeather
import a.alt.z.weather.model.weather.PresentWeather
import a.alt.z.weather.model.location.PresentWeatherByLocation
import a.alt.z.weather.model.location.PreviewPresentWeather

interface WeatherRepository {

    suspend fun getPresentWeather(location: Location): PresentWeather

    suspend fun getPreviewPresentWeather(address: Address): PreviewPresentWeather

    suspend fun getForecastWeather(location: Location): ForecastWeather

    suspend fun getPresentWeathersByLocations(locations: List<Location>): List<PresentWeatherByLocation>
}