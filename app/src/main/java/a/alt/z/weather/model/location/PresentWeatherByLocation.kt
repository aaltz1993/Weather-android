package a.alt.z.weather.model.location

import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.PresentWeather

data class PresentWeatherByLocation(
    val weather: PresentWeather,
    val location: Location
)
