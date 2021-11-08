package a.alt.z.weather.data.api.service

import a.alt.z.weather.BuildConfig
import a.alt.z.weather.data.api.model.address.CoordinateToAddressResponse
import a.alt.z.weather.data.api.model.address.SearchAddressResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query

interface KakaoService {

    @GET("search/address.json")
    suspend fun searchAddress(
        @Query("query") query: String,
        @Query("size") size: Int = 30
    ): SearchAddressResponse

    @GET("geo/coord2address.json")
    suspend fun coordinateToAddress(
        @Query("y") latitude: Double,
        @Query("x") longitude: Double
    ): CoordinateToAddressResponse

    companion object {
        private const val BASE_URL = "https://dapi.kakao.com//v2/local/"

        fun create(converterFactory: Converter.Factory): KakaoService {
            val client = OkHttpClient.Builder()
                .addInterceptor { chain ->
                    val request = chain.request()
                        .newBuilder()
                        .addHeader("Authorization", BuildConfig.KAKAO_API_KEY)
                        .build()

                    chain.proceed(request)
                }
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(KakaoService::class.java)
        }
    }
}