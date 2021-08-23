package a.alt.z.weather.domain.repository

import a.alt.z.weather.model.HourlyForecast
import a.alt.z.weather.model.WeeklyForecast

interface WeatherRepository {

    suspend fun getHourlyForecast(baseDate: String, baseTime: String, x: Int, y: Int): List<HourlyForecast>

    suspend fun getWeeklyForecast(baseDate: String, baseTime: String, x: Int, y: Int): List<WeeklyForecast>
}