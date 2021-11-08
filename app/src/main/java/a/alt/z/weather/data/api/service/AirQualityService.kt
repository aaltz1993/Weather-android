package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.airquality.PresentAirQualityResponse
import a.alt.z.weather.data.api.model.airquality.AirQualityForecastResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface AirQualityService {

    @GET("getCtprvnRltmMesureDnsty")
    suspend fun getPresentAirQuality(
        @Query("returnType") returnType: String = "json",
        @Query("ver") version: String = "1.0",
        @Query("numOfRows") numberOfRows: Int = 100,
        @Query("sidoName") sidoName: String
    ): PresentAirQualityResponse

    @GET("getMinuDustFrcstDspth")
    suspend fun getAirQualityForecast(
        @Query("returnType") returnType: String = "json",
        /* 2021-08-29 */
        @Query("searchDate") date: String
    ): AirQualityForecastResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): AirQualityService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(AirQualityService::class.java)
        }
    }
}