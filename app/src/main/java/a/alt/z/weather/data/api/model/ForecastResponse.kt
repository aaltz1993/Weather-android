package a.alt.z.weather.data.api.model

data class ForecastResponse(
    val response: ForecastHeaderBody
)

data class ForecastHeaderBody(
    val header: ForecastHeader,
    val body: ForecastBody
)