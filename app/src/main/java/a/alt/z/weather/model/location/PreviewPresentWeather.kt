package a.alt.z.weather.model.location

import a.alt.z.weather.model.weather.PresentWeather

data class PreviewPresentWeather(
    val address: Address,
    val weather: PresentWeather
)
