package a.alt.z.weather.di

import a.alt.z.weather.data.api.service.interceptor.AuthenticationInterceptor
import a.alt.z.weather.data.api.service.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides @Singleton
    fun providesInterceptor(): Interceptor {
        return AuthenticationInterceptor()
    }

    @Provides @Singleton
    fun providesClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .connectTimeout(30L, TimeUnit.SECONDS)
            .readTimeout(30L, TimeUnit.SECONDS)
            .build()
    }

    @Provides @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides @Singleton
    fun providesHourlyForecastService(client: OkHttpClient, converterFactory: Converter.Factory): HourlyForecastService {
        return HourlyForecastService.create(client, converterFactory)
    }

    @Provides @Singleton
    fun providesAirPollutionService(client: OkHttpClient, converterFactory: Converter.Factory): AirQualityService {
        return AirQualityService.create(client, converterFactory)
    }

    @Provides @Singleton
    fun providesDailyForecastService(client: OkHttpClient, converterFactory: Converter.Factory): DailyForecastService {
        return DailyForecastService.create(client, converterFactory)
    }

    @Provides @Singleton
    fun providesLivingWeatherIndexService(client: OkHttpClient, converterFactory: Converter.Factory): LivingWeatherIndexService {
        return LivingWeatherIndexService.create(client, converterFactory)
    }

    @Provides @Singleton
    fun providesKakaoService(converterFactory: Converter.Factory): KakaoService {
        return KakaoService.create(converterFactory)
    }

    @Provides @Singleton
    fun providesSunriseSunsetService(client: OkHttpClient): SunriseSunsetService {
        return SunriseSunsetService.create(client)
    }
}