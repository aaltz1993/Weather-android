package a.alt.z.weather.data.database.dao

import a.alt.z.weather.data.database.model.UVIndexEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDate

@Dao
interface UVIndexDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUVIndices(uvIndices: List<UVIndexEntity>)

    @Query("SELECT * FROM uvIndex WHERE locationId = :locationId AND date = :date")
    fun getUVIndex(locationId: Long, date: LocalDate): UVIndexEntity?
}