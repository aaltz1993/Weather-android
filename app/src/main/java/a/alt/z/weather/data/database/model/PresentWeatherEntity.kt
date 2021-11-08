package a.alt.z.weather.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDateTime

@Entity(
    tableName = "presentWeather",
    indices = [
        Index("locationId", unique = true)
    ]
)
data class PresentWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val locationId: Long,
    val skyCode: Int,
    val temperature: Int,
    val precipitationTypeCode: Int,
    val precipitation: Float,
    val windSpeed: Float,
    val windDirection: Int,
    val humidity: Int,
    val fineParticleValue: Int,
    val ultraFineParticleValue: Int,
    val baseDateTime: LocalDateTime
)
