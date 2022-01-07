package a.alt.z.weather.model.appwidget

data class AppWidgetConfigure(
    val widgetID: Int,
    val locationID: Long,
    val uiMode: AppWidgetUIMode,
    val transparency: Int
)
