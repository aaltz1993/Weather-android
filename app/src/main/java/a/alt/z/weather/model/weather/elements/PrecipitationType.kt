package a.alt.z.weather.model.weather.elements

enum class PrecipitationType {
    NONE,
    RAIN,
    RAIN_SNOW,
    SNOW,
    SHOWER;
    /*
    DRIZZLE,
    DRIZZLE_SLEET,
    SLEET;
     */

    val code: Int get() = when (this) {
        NONE -> 0
        RAIN -> 1
        RAIN_SNOW -> 2
        SNOW -> 3
        SHOWER -> 4
    }

    companion object {

        fun codeOf(code: Int): PrecipitationType {
            return when (code) {
                0 -> NONE
                1 -> RAIN
                2 -> RAIN_SNOW
                3 -> SNOW
                4 -> SHOWER
                /*
                5 -> DRIZZLE
                6 -> DRIZZLE_SLEET
                7 -> SLEET
                 */
                else -> NONE
            }
        }
    }
}