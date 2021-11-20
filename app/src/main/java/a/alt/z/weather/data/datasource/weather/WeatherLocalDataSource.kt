package a.alt.z.weather.data.datasource.weather

import a.alt.z.weather.data.database.model.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

interface WeatherLocalDataSource {

    suspend fun savePresentWeather(presentWeather: PresentWeatherEntity)

    suspend fun getPresentWeather(
        locationId: Long,
        baseDateTime: LocalDateTime
    ): PresentWeatherEntity?

    suspend fun saveHourlyWeathers(hourlyWeathers: List<HourlyWeatherEntity>)

    suspend fun getHourlyWeathers(
        locationId: Long,
        from: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Seoul")).withMinute(0).withSecond(0).withNano(0),
        updatedAt: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))
    ): List<HourlyWeatherEntity>

    suspend fun saveDailyWeathers(dailyWeathers: List<DailyWeatherEntity>)

    suspend fun getDailyWeathers(
        locationId: Long,
        from: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul")),
        until: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul")).plusDays(7)
    ): List<DailyWeatherEntity>

    suspend fun deleteWeatherData(locationId: Long)

    suspend fun saveSunriseSunset(sunriseSunset: SunriseSunsetEntity)

    suspend fun getSunriseSunset(locationId: Long, date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))): SunriseSunsetEntity?

    suspend fun saveUVIndices(uvIndices: List<UVIndexEntity>)

    suspend fun getUVIndex(locationId: Long, date: LocalDate = LocalDate.now(ZoneId.of("Asia/Seoul"))): UVIndexEntity?
}