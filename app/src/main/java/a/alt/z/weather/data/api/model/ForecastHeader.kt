package a.alt.z.weather.data.api.model

import com.google.gson.annotations.SerializedName

data class ForecastHeader(
        val resultCode: Int,
        @SerializedName("resultMsg")
        val resultMessage: String
)