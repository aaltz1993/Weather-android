package a.alt.z.weather.data.api.model.srt

import com.google.gson.annotations.SerializedName

data class ForecastResponse(
    val response: ForecastHeaderBody
)

data class ForecastHeaderBody(
    val header: ForecastHeader,
    val body: ForecastBody
)

data class ForecastHeader(
    val resultCode: Int,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class ForecastBody(
    val dataType: String,
    val items: ForecastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class ForecastItems(
    @SerializedName("item")
    val list: List<ForecastItem>
)

data class ForecastItem(
    val baseDate: String,
    val baseTime: String,
    @SerializedName("fcstDate") val date: String,
    @SerializedName("fcstTime") val time: String,
    @SerializedName("fcstValue") val observedValue: String,
    val category: String,
    @SerializedName("nx") val x: Int,
    @SerializedName("ny") val y: Int

)