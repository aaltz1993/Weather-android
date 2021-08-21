package a.alt.z.weather.data.api.model

import com.google.gson.annotations.SerializedName

data class AirHeader(
        val resultCode: Int,
        @SerializedName("resultMsg")
        val resultMessage: String
)