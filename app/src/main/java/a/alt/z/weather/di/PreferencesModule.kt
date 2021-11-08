package a.alt.z.weather.di

import a.alt.z.weather.data.preferences.PreferencesStorage
import a.alt.z.weather.data.preferences.WeatherPreferencesStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PreferencesModule {

    @Binds @Singleton
    abstract fun bindsWeatherPreferencesStorage(preferencesStorage: WeatherPreferencesStorage): PreferencesStorage
}