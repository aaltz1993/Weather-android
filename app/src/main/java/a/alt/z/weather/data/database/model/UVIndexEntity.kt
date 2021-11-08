package a.alt.z.weather.data.database.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import org.threeten.bp.LocalDate

@Entity(
    tableName = "uvIndex",
    indices = [
        Index(
            value = ["locationId", "date"],
            unique = true
        )
    ]
)
data class UVIndexEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val locationId: Long,
    val date: LocalDate,
    val index: Int
)