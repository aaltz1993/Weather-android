package a.alt.z.weather.data.api

import a.alt.z.weather.data.api.model.mid.MidLandForecastResponse
import a.alt.z.weather.data.api.model.mid.MidTemperatureResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface MidForecastService {

    /* 주간 강수확률 + 태그 */
    @GET("getMidLandFcst")
    suspend fun getMidLandForecast(
        @Query("dataType") dataType: String = "JSON",
        @Query("regId") regionId: String,
        @Query("tmFc") dateTime: String
    ): MidLandForecastResponse

    /* 주간 최저 / 최고 기온 */
    @GET("getMidTa")
    suspend fun getMidTemperature(
        @Query("dataType") dataType: String = "JSON",
        @Query("regId") regionId: String,
        @Query("tmFc") dateTime: String
    ): MidTemperatureResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1360000/MidFcstInfoService/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): MidForecastService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(MidForecastService::class.java)
        }
    }
}