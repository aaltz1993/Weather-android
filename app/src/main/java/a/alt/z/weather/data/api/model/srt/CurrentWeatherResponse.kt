package a.alt.z.weather.data.api.model.srt

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    val response: CurrentWeatherHeaderBody
)

data class CurrentWeatherHeaderBody(
    val header: CurrentWeatherHeader,
    val body: CurrentWeatherBody
)

data class CurrentWeatherHeader(
    val resultCode: Int,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class CurrentWeatherBody(
    val dataType: String,
    val items: CurrentWeatherItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class CurrentWeatherItems(
    @SerializedName("item")
    val list: List<CurrentWeatherItem>
)

data class CurrentWeatherItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    @SerializedName("obsrValue") val observedValue: Double
)