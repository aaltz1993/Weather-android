package a.alt.z.weather.data.api.model

data class ForecastBody(
        val dataType: String,
        val items: ForecastItems,
        val pageNo: Int,
        val numOfRows: Int,
        val totalCount: Int
)