package a.alt.z.weather.di

import a.alt.z.weather.data.datasource.location.LocationLocalDataSource
import a.alt.z.weather.data.datasource.location.LocationLocalDataSourceImpl
import a.alt.z.weather.data.datasource.location.LocationRemoteDataSource
import a.alt.z.weather.data.datasource.location.LocationRemoteDataSourceImpl
import a.alt.z.weather.data.datasource.weather.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds @Singleton
    abstract fun bindsWeatherLocalDataSource(localDataSource: WeatherLocalDataSourceImpl): WeatherLocalDataSource

    @Binds @Singleton
    abstract fun bindsWeatherRemoteDataSource(remoteDataSource: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource

    @Binds @Singleton
    abstract fun bindsLocationLocalDataSource(localDataSource: LocationLocalDataSourceImpl): LocationLocalDataSource

    @Binds @Singleton
    abstract fun bindsLocationRemoteDataSource(remoteDataSource: LocationRemoteDataSourceImpl): LocationRemoteDataSource
}