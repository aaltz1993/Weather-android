package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.airquality.CurrentAirPollutionResponse
import a.alt.z.weather.data.api.model.weather.CurrentWeatherResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface OpenWeatherMapService {

    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") serviceKey: String = "443bd424329399aaed92657f5d1a075e",
        @Query("units") units: String = "metric"
    ): CurrentWeatherResponse

    @GET("air_pollution")
    suspend fun getCurrentAirPollution(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") serviceKey: String = "443bd424329399aaed92657f5d1a075e"
    ): CurrentAirPollutionResponse


    companion object {
        private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

        fun create(converterFactory: Converter.Factory): OpenWeatherMapService {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
                .connectTimeout(30L, TimeUnit.SECONDS)
                .readTimeout(30L, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(OpenWeatherMapService::class.java)
        }
    }
}