package a.alt.z.weather.model

enum class PrecipitationType(code: Int) {
    NONE(0),
    RAIN(1),
    RAIN_SNOW(2),
    SNOW(3),
    SHOWER(4);

    companion object {

        fun valueOf(code: Int): PrecipitationType {
            return when (code) {
                0 -> NONE
                1 -> RAIN
                2 -> RAIN_SNOW
                3 -> SNOW
                4 -> SHOWER
                else -> NONE
            }
        }
    }
}