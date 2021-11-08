package a.alt.z.weather.model.weather.elements

data class UVIndex(
    val index: Int
) {
    val grade: String get() = when (index) {
        in 0..2 -> "낮음"
        in 3..5 -> "보통"
        in 6..7 -> "높음"
        in 8..10 -> "매우 높음"
        in 11..Int.MAX_VALUE -> "위험"
        else -> throw IllegalArgumentException()
    }
}
