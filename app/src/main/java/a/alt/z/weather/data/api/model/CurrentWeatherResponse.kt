package a.alt.z.weather.data.api.model

data class CurrentWeatherResponse(
    val response: CurrentWeatherHeaderBody
)

data class CurrentWeatherHeaderBody(
    val header: CurrentWeatherHeader,
    val body: CurrentWeatherBody
)