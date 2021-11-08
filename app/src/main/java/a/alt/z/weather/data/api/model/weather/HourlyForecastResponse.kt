package a.alt.z.weather.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class HourlyForecastResponse(
    val response: HourlyForecastHeaderBody
)

data class HourlyForecastHeaderBody(
    val header: HourlyForecastHeader,
    val body: HourlyForecastBody
)

data class HourlyForecastHeader(
    val resultCode: Int,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class HourlyForecastBody(
    val dataType: String,
    val items: HourlyForecastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class HourlyForecastItems(
    @SerializedName("item")
    val list: List<HourlyForecastItem>
)

data class HourlyForecastItem(
    val baseDate: String,
    val baseTime: String,
    @SerializedName("fcstDate") val date: String,
    @SerializedName("fcstTime") val time: String,
    @SerializedName("fcstValue") val observedValue: String,
    val category: String,
    @SerializedName("nx") val x: Int,
    @SerializedName("ny") val y: Int

)