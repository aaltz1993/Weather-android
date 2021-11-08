package a.alt.z.weather.data.api.model.airquality

import com.google.gson.annotations.SerializedName

data class AirQualityForecastResponse (
    val response: AirQualityForecastHeaderBody
)

data class AirQualityForecastHeaderBody(
    val header: AirQualityForecastHeader,
    val body: AirQualityForecastBody
)

data class AirQualityForecastHeader(
    @SerializedName("resultMsg")
    val resultMessage: String,
    val resultCode: String
)

data class AirQualityForecastBody(
    val items: List<AirQualityForecastItem>
)

data class AirQualityForecastItem(
    /*
    PM10, PM25, O3
     */
    @SerializedName("informCode") val code: String,
    /*
    "서울 : 좋음,제주 : 좋음,전남 : 좋음,전북 : 좋음,광주 : 좋음,경남 : 좋음,경북 : 좋음,울산 : 보통,대구 : 좋음,부산 : 좋음,충남 : 좋음,충북 : 좋음,세종 : 좋음,대전 : 좋음,영동 : 좋음,영서 : 좋음,경기남부 : 좋음,경기북부 : 좋음,인천 : 좋음"
    */
    @SerializedName("informGrade") val grade: String,
    /*
    "2021-08-29"
     */
    @SerializedName("informData") val date: String
)