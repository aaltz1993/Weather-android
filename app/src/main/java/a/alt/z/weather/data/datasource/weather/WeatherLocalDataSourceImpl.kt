package a.alt.z.weather.data.datasource.weather

import a.alt.z.weather.data.database.dao.SunriseSunsetDao
import a.alt.z.weather.data.database.dao.UVIndexDao
import a.alt.z.weather.data.database.dao.WeatherDao
import a.alt.z.weather.data.database.model.*
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import javax.inject.Inject

class WeatherLocalDataSourceImpl @Inject constructor(
    private val weatherDao: WeatherDao,
    private val sunriseSunsetDao: SunriseSunsetDao,
    private val UVIndexDao: UVIndexDao
): WeatherLocalDataSource {

    override suspend fun savePresentWeather(presentWeather: PresentWeatherEntity) {
        weatherDao.insertPresentWeather(presentWeather)
    }

    override suspend fun getPresentWeather(locationId: Long, baseDateTime: LocalDateTime): PresentWeatherEntity? {
        return weatherDao.getPresentWeather(locationId, baseDateTime)
    }

    override suspend fun saveHourlyWeathers(hourlyWeathers: List<HourlyWeatherEntity>) {
        weatherDao.insertHourlyWeathers(hourlyWeathers)
    }

    override suspend fun getHourlyWeathers(locationId: Long, from: LocalDateTime, updatedAt: LocalDate): List<HourlyWeatherEntity> {
        return weatherDao.getHourlyWeathers(locationId, from, updatedAt)
    }

    override suspend fun saveDailyWeathers(dailyWeathers: List<DailyWeatherEntity>) {
        weatherDao.insertDailyWeathers(dailyWeathers)
    }

    override suspend fun getDailyWeathers(locationId: Long, from: LocalDate, until: LocalDate): List<DailyWeatherEntity> {
        return weatherDao.getDailyWeathers(locationId, from, until)
    }

    override suspend fun deleteWeatherData(locationId: Long) {
        weatherDao.deletePresentWeatherData(locationId)
        weatherDao.deleteHourlyWeatherData(locationId)
        weatherDao.deleteDailyWeatherData(locationId)
    }

    override suspend fun saveSunriseSunset(sunriseSunset: SunriseSunsetEntity) {
        sunriseSunsetDao.saveSunriseSunset(sunriseSunset)
    }

    override suspend fun getSunriseSunset(locationId: Long, date: LocalDate): SunriseSunsetEntity? {
        return sunriseSunsetDao.getSunriseSunset(locationId, date)
    }

    override suspend fun saveUVIndices(uvIndices: List<UVIndexEntity>) {
        UVIndexDao.insertUVIndices(uvIndices)
    }

    override suspend fun getUVIndex(locationId: Long, date: LocalDate): UVIndexEntity? {
        return UVIndexDao.getUVIndex(locationId, date)
    }
}