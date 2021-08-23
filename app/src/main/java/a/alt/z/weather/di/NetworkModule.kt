package a.alt.z.weather.di

import a.alt.z.weather.data.api.AirService
import a.alt.z.weather.data.api.interceptor.AuthInterceptor
import a.alt.z.weather.data.api.MidForecastService
import a.alt.z.weather.data.api.ShortForecastService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun providesInterceptor(): Interceptor {
        return AuthInterceptor()
    }

    @Provides
    @Singleton
    fun providesClient(interceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun providesConverterFactory(): Converter.Factory {
        return GsonConverterFactory.create()
    }

    @Provides
    @Singleton
    fun providesShortForecastService(client: OkHttpClient, converterFactory: Converter.Factory): ShortForecastService {
        return ShortForecastService.create(client, converterFactory)
    }

    @Provides
    @Singleton
    fun providesAirService(client: OkHttpClient, converterFactory: Converter.Factory): AirService {
        return AirService.create(client, converterFactory)
    }

    @Provides
    @Singleton
    fun providesMidForecastService(client: OkHttpClient, converterFactory: Converter.Factory): MidForecastService {
        return MidForecastService.create(client, converterFactory)
    }
}