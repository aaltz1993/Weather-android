package a.alt.z.weather.data.api.model.airquality

import com.google.gson.annotations.SerializedName

data class CurrentAirPollutionResponse(
    val list: List<CurrentAirPollutionResponseItem>
)
data class CurrentAirPollutionResponseItem(
    val dt: Long,
    val components: CurrentAirPollutionComponents
)

data class CurrentAirPollutionComponents(
    @SerializedName("pm2_5") val fineParticleValue: Double,
    @SerializedName("pm10") val ultraFineParticleValue: Double
)