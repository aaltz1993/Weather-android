package a.alt.z.weather.di

import a.alt.z.weather.data.database.WeatherDatabase
import a.alt.z.weather.data.database.dao.LocationDao
import a.alt.z.weather.data.database.dao.SunriseSunsetDao
import a.alt.z.weather.data.database.dao.UVIndexDao
import a.alt.z.weather.data.database.dao.WeatherDao
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherDatabase {
        return WeatherDatabase.getInstance(context)
    }

    @Provides @Singleton
    fun providesWeatherDao(database: WeatherDatabase): WeatherDao {
        return database.weatherDao()
    }

    @Provides @Singleton
    fun providesLocationDao(database: WeatherDatabase): LocationDao {
        return database.locationDao()
    }

    @Provides @Singleton
    fun providesSunriseSunsetDao(database: WeatherDatabase): SunriseSunsetDao {
        return database.sunriseSunsetDao()
    }

    @Provides @Singleton
    fun providesUvraysIndexDao(database: WeatherDatabase): UVIndexDao {
        return database.uvIndexDao()
    }
}