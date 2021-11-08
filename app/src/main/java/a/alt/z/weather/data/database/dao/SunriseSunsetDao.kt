package a.alt.z.weather.data.database.dao

import a.alt.z.weather.data.database.model.SunriseSunsetEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import org.threeten.bp.LocalDate

@Dao
interface SunriseSunsetDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSunriseSunset(sunriseSunset: SunriseSunsetEntity)

    @Query("SELECT * FROM sunriseSunset WHERE date = :date")
    suspend fun getSunriseSunset(date: LocalDate): SunriseSunsetEntity?
}