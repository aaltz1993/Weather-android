package a.alt.z.weather.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(
    tableName = "dailyWeather",
    indices = [
        Index(
            value = ["locationId", "date"],
            unique = true
        )
    ]
)
data class DailyWeatherEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val locationId: Long,
    val date: LocalDate,
    val minTemperature: Int,
    val maxTemperature: Int,
    val skyCodeBeforeNoon: Int,
    val skyCodeAfternoon: Int,
    val probabilityOfPrecipitationBeforeNoon: Int,
    val probabilityOfPrecipitationAfternoon: Int,
    val precipitationCodeBeforeNoon: Int,
    val precipitationCodeAfternoon: Int
)