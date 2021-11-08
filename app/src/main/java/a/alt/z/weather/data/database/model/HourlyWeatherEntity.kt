package a.alt.z.weather.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

@Entity(tableName = "hourlyWeather")
data class HourlyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val locationId: Long,
    val dateTime: LocalDateTime,
    val temperature: Int,
    val minTemperature: Int,
    val maxTemperature: Int,
    val skyCode: Int,
    val probabilityOfPrecipitation: Int,
    val precipitationCode: Int,
    val precipitation: Float,
    val snow: Float,
    val humidity: Int,
    val windDirection: Int,
    val windSpeed: Float,
    val updatedAt: LocalDate
)