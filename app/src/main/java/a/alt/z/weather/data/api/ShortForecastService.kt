package a.alt.z.weather.data.api

import a.alt.z.weather.data.api.model.srt.CurrentWeatherResponse
import a.alt.z.weather.data.api.model.srt.ForecastResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface ShortForecastService {

    /**
     * 초단기실황
     */
    @GET("getUltraSrtNcst")
    suspend fun getCurrentWeather(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 8,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String, /* 정시단위 0700 -> 0740 부터 */
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): CurrentWeatherResponse

    /**
     * 단기예보
     */
    @GET("getVilageFcst")
    suspend fun getForecast(
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 875,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: String,
        @Query("base_time") baseTime: String,
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): ForecastResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): ShortForecastService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(ShortForecastService::class.java)
        }
    }
}