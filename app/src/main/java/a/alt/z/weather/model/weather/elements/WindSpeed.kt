package a.alt.z.weather.model.weather.elements

enum class WindSpeed {
    WEAK,
    LITTLE_STRONG,
    STRONG,
    VERY_STRONG;

    companion object {
        fun speedOf(speed: Float): WindSpeed {
            return when (speed) {
                in 0F..4F -> WEAK
                in 4F..9F -> LITTLE_STRONG
                in 9F..14F -> STRONG
                else -> VERY_STRONG
            }
        }
    }
}