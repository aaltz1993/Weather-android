package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.weather.NowcastResponse
import a.alt.z.weather.data.api.model.weather.HourlyForecastResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyForecastService {

    @GET("getUltraSrtNcst")
    suspend fun getNowcast(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 8,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String, /* 정시단위 0700 -> 0740 부터 */
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): NowcastResponse

    @GET("getUltraSrtFcst")
    suspend fun getHourlyForecast6Hours(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 60,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String, /* 30분단위 0730 -> 0745 부터 */
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): HourlyForecastResponse

    @GET("getVilageFcst")
    suspend fun getHourlyForecast3Days(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 1000,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): HourlyForecastResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): HourlyForecastService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(HourlyForecastService::class.java)
        }
    }
}