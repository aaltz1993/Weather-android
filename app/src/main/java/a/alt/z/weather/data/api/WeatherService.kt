package a.alt.z.weather.data.api

import a.alt.z.weather.data.api.model.CurrentWeatherResponse
import a.alt.z.weather.data.api.model.ForecastResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("getUltraSrtNcst")
    suspend fun getCurrentWeather(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 8,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: Int,
        @Query("base_time") baseTime: Int, /* 정시단위 0700 -> 0740 부터 */
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): CurrentWeatherResponse

    @GET("getVilageFcst")
    suspend fun getForecast(
        @Query("serviceKey") serviceKey: String,
        @Query("pageNo") pageNo: Int = 1,
        @Query("numOfRows") numOfRows: Int = 100,
        @Query("dataType") dataType: String = "JSON",
        @Query("base_date") baseDate: Int,
        @Query("base_time") baseTime: Int,
        @Query("nx") nx: Int,
        @Query("ny") ny: Int
    ): ForecastResponse
}