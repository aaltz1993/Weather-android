package a.alt.z.weather.data.api.model

enum class CurrentWeather(
    val label: String,
    val unit: String,
    var observedValue: Double = 0.0
) {
    T1H("기온", "\u2103"),
    /* 1mm 미만, 정수값 (1~29mm), 30~50mm, 50mm 이상 */
    RN1("1시간 강수량", "mm"),
    /* + 동, - 서 */
    UUU("동서바람성분", "m/s"),
    /* + 북, - 남 */
    VVV("남북바람성분", "m/s"),
    REH("습도", "%"),
    /* (초단기) 없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7) */
    PTY("강수형태", ""),
    VEC("풍향", "deg"),
    WSD("풍속", "m/s")
}

