package a.alt.z.weather.data.api.model.mid

import com.google.gson.annotations.SerializedName

data class MidLandForecastResponse(
    val response: MidLandForecastHeaderBody
)

data class MidLandForecastHeaderBody(
    val header: MidLandForecastHeader,
    val body: MidLandForecastBody
)

data class MidLandForecastHeader(
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class MidLandForecastBody(
    val dataType: String,
    val items: MidLandForecastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class MidLandForecastItems(
    @SerializedName("item")
    val list: List<MidLandForecastItem>
)

data class MidLandForecastItem(
    @SerializedName("rnSt3Am") val precipitation3DaysAfterAm: Int,
    @SerializedName("rnSt3Pm") val precipitation3DaysAfterPm: Int,
    @SerializedName("rnSt4Am") val precipitation4DaysAfterAm: Int,
    @SerializedName("rnSt4Pm") val precipitation4DaysAfterPm: Int,
    @SerializedName("rnSt5Am") val precipitation5DaysAfterAm: Int,
    @SerializedName("rnSt5Pm") val precipitation5DaysAfterPm: Int,
    @SerializedName("rnSt6Am") val precipitation6DaysAfterAm: Int,
    @SerializedName("rnSt6Pm") val precipitation6DaysAfterPm: Int,
    @SerializedName("rnSt7Am") val precipitation7DaysAfterAm: Int,
    @SerializedName("rnSt7Pm") val precipitation7DaysAfterPm: Int,
    @SerializedName("wf3Am") val description3DaysAfterAm: String,
    @SerializedName("wf3Pm") val description3DaysAfterPm: String,
    @SerializedName("wf4Am") val description4DaysAfterAm: String,
    @SerializedName("wf4Pm") val description4DaysAfterPm: String,
    @SerializedName("wf5Am") val description5DaysAfterAm: String,
    @SerializedName("wf5Pm") val description5DaysAfterPm: String,
    @SerializedName("wf6Am") val description6DaysAfterAm: String,
    @SerializedName("wf6Pm") val description6DaysAfterPm: String,
    @SerializedName("wf7Am") val description7DaysAfterAm: String,
    @SerializedName("wf7Pm") val description7DaysAfterPm: String
)