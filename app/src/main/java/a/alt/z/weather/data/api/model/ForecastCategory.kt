package a.alt.z.weather.data.api.model

/* forecast */
enum class ForecastCategory {
    POP,
    /*
    초단기
    없음(0), 비(1), 비/눈(2), 눈(3), 빗방울(5), 빗방울눈날림(6), 눈날림(7)
    단기
    없음(0), 비(1), 비/눈(2), 눈(3), 소나기(4)
    */
    PTY,
    PCP,
    REH,
    SNO,
    SKY, /* 맑음(1), 구름많음(3), 흐림(4) */
    TMP,
    TMN,
    TMX,
    UUU,
    VVV,
    WAV,
    VEC,
    WSD
}

/* ultra short forecast */
