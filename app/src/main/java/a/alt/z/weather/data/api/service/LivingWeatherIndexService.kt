package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.uvindex.UVIndexResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface LivingWeatherIndexService {

    @GET("getUVIdxV2")
    suspend fun getUVIndex(
        @Query("pageNo") pageNo: Int = 1,
        @Query("dataType") dataType: String = "JSON",
        @Query("areaNo") areaNo: String,
        @Query("time") time: String
    ): UVIndexResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/1360000/LivingWthrIdxServiceV2/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): LivingWeatherIndexService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(LivingWeatherIndexService::class.java)
        }
    }
}