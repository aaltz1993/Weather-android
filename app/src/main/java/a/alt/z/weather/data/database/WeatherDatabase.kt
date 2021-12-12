package a.alt.z.weather.data.database

import a.alt.z.weather.data.database.converter.Converters
import a.alt.z.weather.data.database.dao.WeatherDao
import a.alt.z.weather.data.database.dao.LocationDao
import a.alt.z.weather.data.database.dao.SunriseSunsetDao
import a.alt.z.weather.data.database.dao.UVIndexDao
import a.alt.z.weather.data.database.model.*
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [
        LocationEntity::class,
        PresentWeatherEntity::class,
        HourlyWeatherEntity::class,
        DailyWeatherEntity::class,
        SunriseSunsetEntity::class,
        UVIndexEntity::class
               ],
    version = 3
)
@TypeConverters(Converters::class)
abstract class WeatherDatabase: RoomDatabase() {

    abstract fun locationDao(): LocationDao

    abstract fun weatherDao(): WeatherDao

    abstract fun sunriseSunsetDao(): SunriseSunsetDao

    abstract fun uvIndexDao(): UVIndexDao

    companion object {
        private const val NAME = "weather_database"

        @Volatile private var database: WeatherDatabase? = null

        fun getInstance(context: Context): WeatherDatabase = database ?: synchronized(this) {
            Room.databaseBuilder(context.applicationContext, WeatherDatabase::class.java, NAME)
                .fallbackToDestructiveMigration()
                .build()
                .also { database = it }
        }
    }
}
