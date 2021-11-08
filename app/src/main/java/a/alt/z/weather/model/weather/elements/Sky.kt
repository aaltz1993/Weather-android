package a.alt.z.weather.model.weather.elements

enum class Sky {
    /* 맑음 */
    CLEAR,
    /* 구름많음 */
    CLOUDY,
    /* 흐림 */
    OVERCAST;

    val code: Int get() {
        return when (this) {
            CLEAR -> 1
            CLOUDY -> 3
            OVERCAST -> 4
        }
    }

    companion object {
        fun codeOf(code: Int): Sky {
            return when (code) {
                1 -> CLEAR
                3 -> CLOUDY
                4 -> OVERCAST
                else -> throw IllegalArgumentException()
            }
        }
    }
}