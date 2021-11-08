package a.alt.z.weather.data.api.model.airquality

import com.google.gson.annotations.SerializedName

data class PresentAirQualityResponse(
    val response: PresentAirQualityHeaderBody
)

data class PresentAirQualityHeaderBody(
    val header: PresentAirQualityHeader,
    val body: PresentAirQualityBody
)

data class PresentAirQualityHeader(
    @SerializedName("resultMsg")
    val resultMessage: String,
    val resultCode: String
)

data class PresentAirQualityBody(
    val items: List<PresentAirQualityItem>
)

data class PresentAirQualityItem(
    val sidoName: String,
    val stationName: String,
    /* 2021-08-29 21:00 */
    @SerializedName("dataTime") val dateTime: String,
    val pm10Value: String?,
    val pm10Grade: Int?,
    val pm25Value: String?,
    val pm25Grade: Int?,
    /* 아황산가스 ppm */
    val so2Value: String?,
    val so2Grade: Int?,
    /* 일산화탄소 ppm */
    val coValue: String?,
    val coGrade: Int?,
    /* 오존 ppm */
    val o3Value: String?,
    val o3Grade: Int?,
    /* 이산화질소 ppm */
    val no2Value: String?,
    val no2Grade: Int?,
    /* 통합대기환경수치 0-50 좋음, 51-100 보통, 101-250 나쁨, 251- 매우나쁨 */
    val khaiValue: String?,
    val khaiGrade: Int?
)