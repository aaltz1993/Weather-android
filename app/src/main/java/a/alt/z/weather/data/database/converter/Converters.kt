package a.alt.z.weather.data.database.converter

import androidx.room.TypeConverter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneOffset

object Converters {

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    @TypeConverter
    fun toEpochDay(date: LocalDate): Long = date.toEpochDay()

    @TypeConverter
    fun toLocalDateTime(epochSecond: Long): LocalDateTime = LocalDateTime.ofEpochSecond(epochSecond, 0, ZoneOffset.of("+09:00"))

    @TypeConverter
    fun toEpochSecond(dateTime: LocalDateTime) = dateTime.toEpochSecond(ZoneOffset.of("+09:00"))
}