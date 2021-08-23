package a.alt.z.weather.di

import a.alt.z.weather.domain.repository.WeatherRepository
import a.alt.z.weather.domain.usecase.main.GetHourlyForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun providesGetHourlyForecastUseCase(@IODispatcher coroutineDispatcher: CoroutineDispatcher,
                                         repository: WeatherRepository): GetHourlyForecastUseCase {
        return GetHourlyForecastUseCase(coroutineDispatcher, repository)
    }
}