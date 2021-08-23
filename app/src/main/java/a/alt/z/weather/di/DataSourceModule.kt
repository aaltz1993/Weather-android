package a.alt.z.weather.di

import a.alt.z.weather.data.datasource.WeatherLocalDataSource
import a.alt.z.weather.data.datasource.WeatherLocalDataSourceImpl
import a.alt.z.weather.data.datasource.WeatherRemoteDataSource
import a.alt.z.weather.data.datasource.WeatherRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindsWeatherLocalDataSource(localDataSource: WeatherLocalDataSourceImpl): WeatherLocalDataSource

    @Binds
    @Singleton
    abstract fun bindsWeatherRemoteDataSource(remoteDataSource: WeatherRemoteDataSourceImpl): WeatherRemoteDataSource
}