package a.alt.z.weather.di

import a.alt.z.weather.data.repository.LocationRepositoryImpl
import a.alt.z.weather.data.repository.WeatherRepositoryImpl
import a.alt.z.weather.domain.repository.LocationRepository
import a.alt.z.weather.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindsWeatherRepository(repository: WeatherRepositoryImpl): WeatherRepository

    @Binds @Singleton
    abstract fun bindsLocationRepository(repositoryImpl: LocationRepositoryImpl): LocationRepository
}