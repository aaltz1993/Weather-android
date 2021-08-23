package a.alt.z.weather.model

enum class Sky(code: Int) {
    /* 맑음 */
    CLEAR(1),
    /* 구름많음 */
    CLOUDY(3),
    /* 흐림 */
    OVERCAST(4);

    companion object {

        fun valueOf(code: Int): Sky {
            return when (code) {
                1 -> CLEAR
                3 -> CLOUDY
                4 -> OVERCAST
                else -> CLEAR
            }
        }
    }
}