package a.alt.z.weather.data.api.service

import a.alt.z.weather.data.api.model.sunrisesunset.SunriseSunsetResponse
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface SunriseSunsetService {

    @GET("getAreaRiseSetInfo")
    suspend fun getSunriseSunset(
        @Query("locdate") date: String,
        @Query("location") area: String
    ) : SunriseSunsetResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/B090041/openapi/service/RiseSetInfoService/"

        fun create(client: OkHttpClient): SunriseSunsetService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(
                    TikXmlConverterFactory.create(
                        TikXml.Builder()
                            .exceptionOnUnreadXml(false)
                            .build()
                    )
                )
                .build()
                .create(SunriseSunsetService::class.java)
        }
    }
}