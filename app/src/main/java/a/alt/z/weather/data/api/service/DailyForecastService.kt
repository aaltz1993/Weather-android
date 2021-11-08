package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.weather.DailyForecastResponse
import a.alt.z.weather.data.api.model.weather.DailyTemperatureResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyForecastService {

    /* 주간 강수확률 + 태그 */
    @GET("getMidLandFcst")
    suspend fun getDailyForecast(
        @Query("dataType") dataType: String = "JSON",
        @Query("regId") regionId: String,
        @Query("tmFc") dateTime: String
    ): DailyForecastResponse

    /* 주간 최저 / 최고 기온 */
    @GET("getMidTa")
    suspend fun getDailyTemperature(
        @Query("dataType") dataType: String = "JSON",
        @Query("regId") regionId: String,
        @Query("tmFc") dateTime: String
    ): DailyTemperatureResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1360000/MidFcstInfoService/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): DailyForecastService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(DailyForecastService::class.java)
        }
    }
}