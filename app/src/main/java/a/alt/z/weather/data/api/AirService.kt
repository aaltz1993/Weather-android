package a.alt.z.weather.data.api

import a.alt.z.weather.data.api.model.air.AirResponse
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface AirService {

    @GET("getMsrstnAcctoRltmMesureDnsty")
    suspend fun getCurrent(
            @Query("serviceKey") serviceKey: String,
            @Query("returnType") returnType: String = "json",
    ): AirResponse

    companion object {
        private const val BASE_URL = "http://apis.data.go.kr/B552584/ArpltnInforInqireSvc/"

        fun create(client: OkHttpClient, converterFactory: Converter.Factory): AirService {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(converterFactory)
                .build()
                .create(AirService::class.java)
        }
    }
}
/*
{
    "response": {
        "body": {
            "totalCount": 23,
            "items": [{
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "70",
                "so2Value": "0.003",
                "coValue": "0.3",
                "pm10Flag": null,
                "pm10Value": "20",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 18:00",
                "coGrade": "1",
                "no2Value": "0.015",
                "pm10Grade": "1",
                "o3Value": "0.054"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "73",
                "so2Value": "0.003",
                "coValue": "0.3",
                "pm10Flag": null,
                "pm10Value": "20",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 17:00",
                "coGrade": "1",
                "no2Value": "0.016",
                "pm10Grade": "1",
                "o3Value": "0.057"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "76",
                "so2Value": "0.003",
                "coValue": "0.3",
                "pm10Flag": null,
                "pm10Value": "14",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 16:00",
                "coGrade": "1",
                "no2Value": "0.012",
                "pm10Grade": "1",
                "o3Value": "0.061"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "83",
                "so2Value": "0.004",
                "coValue": "0.4",
                "pm10Flag": null,
                "pm10Value": "23",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 15:00",
                "coGrade": "1",
                "no2Value": "0.015",
                "pm10Grade": "1",
                "o3Value": "0.070"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "139",
                "so2Value": "0.005",
                "coValue": "0.5",
                "pm10Flag": null,
                "pm10Value": "32",
                "o3Grade": "3",
                "khaiGrade": "3",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 14:00",
                "coGrade": "1",
                "no2Value": "0.019",
                "pm10Grade": "1",
                "o3Value": "0.106"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "87",
                "so2Value": "0.005",
                "coValue": "0.5",
                "pm10Flag": null,
                "pm10Value": "28",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 13:00",
                "coGrade": "1",
                "no2Value": "0.021",
                "pm10Grade": "1",
                "o3Value": "0.074"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "66",
                "so2Value": "0.004",
                "coValue": "0.4",
                "pm10Flag": null,
                "pm10Value": "21",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 12:00",
                "coGrade": "1",
                "no2Value": "0.021",
                "pm10Grade": "1",
                "o3Value": "0.049"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "53",
                "so2Value": "0.004",
                "coValue": "0.4",
                "pm10Flag": null,
                "pm10Value": "16",
                "o3Grade": "2",
                "khaiGrade": "2",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 11:00",
                "coGrade": "1",
                "no2Value": "0.020",
                "pm10Grade": "1",
                "o3Value": "0.034"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "50",
                "so2Value": "0.003",
                "coValue": "0.4",
                "pm10Flag": null,
                "pm10Value": "13",
                "o3Grade": "1",
                "khaiGrade": "1",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 10:00",
                "coGrade": "1",
                "no2Value": "0.015",
                "pm10Grade": "1",
                "o3Value": "0.030"
            }, {
                "so2Grade": "1",
                "coFlag": null,
                "khaiValue": "38",
                "so2Value": "0.003",
                "coValue": "0.4",
                "pm10Flag": null,
                "pm10Value": "13",
                "o3Grade": "1",
                "khaiGrade": "1",
                "no2Flag": null,
                "no2Grade": "1",
                "o3Flag": null,
                "so2Flag": null,
                "dataTime": "2021-08-10 09:00",
                "coGrade": "1",
                "no2Value": "0.018",
                "pm10Grade": "1",
                "o3Value": "0.023"
            }],
            "pageNo": 1,
            "numOfRows": 10
        },
        "header": {
            "resultMsg": "NORMAL_CODE",
            "resultCode": "00"
        }
    }
}
 */