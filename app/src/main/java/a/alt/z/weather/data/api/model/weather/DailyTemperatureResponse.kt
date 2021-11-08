package a.alt.z.weather.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class DailyTemperatureResponse(
    val response: DailyTemperatureHeaderBody
)

data class DailyTemperatureHeaderBody(
    val header: DailyTemperatureHeader,
    val body: DailyTemperatureBody
)

data class DailyTemperatureHeader(
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class DailyTemperatureBody(
    val dataType: String,
    val items: DailyTemperatureItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class DailyTemperatureItems(
    @SerializedName("item")
    val list: List<DailyTemperatureItem>
)

data class DailyTemperatureItem(
    @SerializedName("taMin3")
    val minTemperature3DaysAfter: Int,
    @SerializedName("taMax3")
    val maxTemperature3DaysAfter: Int,
    @SerializedName("taMin4")
    val minTemperature4DaysAfter: Int,
    @SerializedName("taMax4")
    val maxTemperature4DaysAfter: Int,
    @SerializedName("taMin5")
    val minTemperature5DaysAfter: Int,
    @SerializedName("taMax5")
    val maxTemperature5DaysAfter: Int,
    @SerializedName("taMin6")
    val minTemperature6DaysAfter: Int,
    @SerializedName("taMax6")
    val maxTemperature6DaysAfter: Int,
    @SerializedName("taMin7")
    val minTemperature7DaysAfter: Int,
    @SerializedName("taMax7")
    val maxTemperature7DaysAfter: Int,
    @SerializedName("taMin8")
    val minTemperature8DaysAfter: Int,
    @SerializedName("taMax8")
    val maxTemperature8DaysAfter: Int,
    @SerializedName("taMin9")
    val minTemperature9DaysAfter: Int,
    @SerializedName("taMax9")
    val maxTemperature9DaysAfter: Int,
    @SerializedName("taMin10")
    val minTemperature10DaysAfter: Int,
    @SerializedName("taMax10")
    val maxTemperature10DaysAfter: Int
)
/*
{
  "regId":"11B10101",
  "taMin3":21,
  "taMin3Low":2,
  "taMin3High":1,
  "taMax3":29,
  "taMax3Low":1,
  "taMax3High":1,
  "taMin4":20,
  "taMin4Low":3,
  "taMin4High":1,
  "taMax4":30,
  "taMax4Low":1,
  "taMax4High":1,
  "taMin5":21,
  "taMin5Low":2,
  "taMin5High":2,
  "taMax5":30,
  "taMax5Low":1,
  "taMax5High":1,
  "taMin6":21,
  "taMin6Low":1,
  "taMin6High":2,
  "taMax6":29,
  "taMax6Low":1,
  "taMax6High":1,
  "taMin7":21,
  "taMin7Low":1,
  "taMin7High":1,
  "taMax7":27,
  "taMax7Low":1,
  "taMax7High":1,
  "taMin8":20,
  "taMin8Low":0,
  "taMin8High":1,
  "taMax8":27,
  "taMax8Low":0,
  "taMax8High":2,
  "taMin9":18,
  "taMin9Low":0,
  "taMin9High":2,
  "taMax9":26,
  "taMax9Low":0,
  "taMax9High":1,
  "taMin10":19,
  "taMin10Low":0,
  "taMin10High":1,
  "taMax10":27,
  "taMax10Low":0,
  "taMax10High":1
}
 */