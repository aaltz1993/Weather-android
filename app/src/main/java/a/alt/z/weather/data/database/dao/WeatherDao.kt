package a.alt.z.weather.data.database.dao

import a.alt.z.weather.data.database.model.DailyWeatherEntity
import a.alt.z.weather.data.database.model.HourlyWeatherEntity
import a.alt.z.weather.data.database.model.PresentWeatherEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPresentWeather(presentWeather: PresentWeatherEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHourlyWeathers(hourlyWeathers: List<HourlyWeatherEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyWeathers(dailyWeathers: List<DailyWeatherEntity>)

    @Query("SELECT * FROM presentWeather WHERE locationId = :locationId AND baseDateTime = :baseDateTime")
    suspend fun getPresentWeather(locationId: Long, baseDateTime: LocalDateTime): PresentWeatherEntity?

    @Query("SELECT * FROM hourlyWeather WHERE locationId = :locationId AND dateTime >= :now AND updatedAt = :updatedAt")
    suspend fun getHourlyWeathers(locationId: Long, now: LocalDateTime, updatedAt: LocalDate): List<HourlyWeatherEntity>

    @Query("SELECT * FROM hourlyWeather WHERE locationId = :locationId AND dateTime = :at")
    suspend fun getHourlyWeather(locationId: Long, at: LocalDateTime): HourlyWeatherEntity

    @Query("SELECT * FROM dailyWeather WHERE locationId = :locationId AND date >= :now AND date < :after7Days")
    suspend fun getDailyWeathers(locationId: Long, now: LocalDate, after7Days: LocalDate): List<DailyWeatherEntity>

    @Query("DELETE FROM hourlyWeather WHERE locationId = :locationId")
    suspend fun deleteHourlyWeatherData(locationId: Long)

    @Query("DELETE FROM dailyWeather WHERE locationId = :locationId")
    suspend fun deleteDailyWeatherData(locationId: Long)

    @Query("DELETE FROM presentWeather WHERE locationId = :locationId")
    suspend fun deletePresentWeatherData(locationId: Long)
}