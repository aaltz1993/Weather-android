package a.alt.z.weather.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class DailyForecastResponse(
    val response: DailyForecastHeaderBody
)

data class DailyForecastHeaderBody(
    val header: DailyForecastHeader,
    val body: DailyForecastBody
)

data class DailyForecastHeader(
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class DailyForecastBody(
    val dataType: String,
    val items: DailyForecastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class DailyForecastItems(
    @SerializedName("item")
    val list: List<DailyForecastItem>
)

data class DailyForecastItem(
    @SerializedName("rnSt3Am") val pop3DaysAfterAm: Int,
    @SerializedName("rnSt3Pm") val pop3DaysAfterPm: Int,
    @SerializedName("rnSt4Am") val pop4DaysAfterAm: Int,
    @SerializedName("rnSt4Pm") val pop4DaysAfterPm: Int,
    @SerializedName("rnSt5Am") val pop5DaysAfterAm: Int,
    @SerializedName("rnSt5Pm") val pop5DaysAfterPm: Int,
    @SerializedName("rnSt6Am") val pop6DaysAfterAm: Int,
    @SerializedName("rnSt6Pm") val pop6DaysAfterPm: Int,
    @SerializedName("rnSt7Am") val pop7DaysAfterAm: Int,
    @SerializedName("rnSt7Pm") val pop7DaysAfterPm: Int,
    @SerializedName("wf3Am") val forecast3DaysAfterAm: String,
    @SerializedName("wf3Pm") val forecast3DaysAfterPm: String,
    @SerializedName("wf4Am") val forecast4DaysAfterAm: String,
    @SerializedName("wf4Pm") val forecast4DaysAfterPm: String,
    @SerializedName("wf5Am") val forecast5DaysAfterAm: String,
    @SerializedName("wf5Pm") val forecast5DaysAfterPm: String,
    @SerializedName("wf6Am") val forecast6DaysAfterAm: String,
    @SerializedName("wf6Pm") val forecast6DaysAfterPm: String,
    @SerializedName("wf7Am") val forecast7DaysAfterAm: String,
    @SerializedName("wf7Pm") val forecast7DaysAfterPm: String
)
/*
{
  "regId":"11B00000",
  "rnSt3Am":20,
  "rnSt3Pm":0,
  "rnSt4Am":0,
  "rnSt4Pm":0,
  "rnSt5Am":0,
  "rnSt5Pm":10,
  "rnSt6Am":10,
  "rnSt6Pm":20,
  "rnSt7Am":30,
  "rnSt7Pm":30,
  "rnSt8":30,
  "rnSt9":30,
  "rnSt10":40,
  "wf3Am":"구름많음",
  "wf3Pm":"맑음",
  "wf4Am":"맑음",
  "wf4Pm":"맑음",
  "wf5Am":"맑음",
  "wf5Pm":"맑음",
  "wf6Am":"맑음",
  "wf6Pm":"맑음",
  "wf7Am":"구름많음",
  "wf7Pm":"구름많음",
  "wf8":"구름많음",
  "wf9":"구름많음",
  "wf10":"흐림"
}
 */