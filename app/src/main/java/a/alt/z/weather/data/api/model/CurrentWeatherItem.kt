package a.alt.z.weather.data.api.model

import com.google.gson.annotations.SerializedName

data class CurrentWeatherItems(
    @SerializedName("item")
    val items: List<CurrentWeatherItem>
)

data class CurrentWeatherItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    @SerializedName("obsrValue")
    val observedValue: Double
)
