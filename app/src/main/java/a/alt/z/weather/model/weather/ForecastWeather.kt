package a.alt.z.weather.model.weather

import a.alt.z.weather.model.weather.elements.SunriseSunset
import a.alt.z.weather.model.weather.elements.UVIndex

data class ForecastWeather(
    val hourlyWeathers: List<HourlyWeather>,
    val dailyWeathers: List<DailyWeather>,
    val uvIndex: UVIndex,
    val sunriseSunset: SunriseSunset
)
