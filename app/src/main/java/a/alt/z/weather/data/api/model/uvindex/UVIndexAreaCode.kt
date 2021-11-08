package a.alt.z.weather.data.api.model.uvindex

enum class UVIndexAreaCode(val code: String) {
    SEOUL("1100000000"),
    BUSAN("2600000000"),
    DAEGU("2700000000"),
    INCHEON("2800000000"),
    GWANGJU("2900000000"),
    DAEJEON("3000000000"),
    ULSAN("3100000000"),
    SEJONG("3600000000"),
    GYEONGGIDO("4100000000"),
    GANGWONDO("4200000000"),
    CHUNGCHEONGBUKDO("4300000000"),
    CHUNGCHEONGNAMDO("4400000000"),
    JEOLLABUKDO("4500000000"),
    JEOLLANAMDO("4600000000"),
    GYEONGSANGBUKDO("4700000000"),
    GYEONGSANGNAMDO("4800000000"),
    JEJU("5000000000");

    companion object {

        fun addressOf(regionDepth1Name: String): UVIndexAreaCode {
            return when (regionDepth1Name) {
                "서울" -> SEOUL
                "부산" -> BUSAN
                "대구" -> DAEGU
                "인천" -> INCHEON
                "광주" -> GWANGJU
                "대전" -> DAEJEON
                "울산" -> ULSAN
                "세종특별자치시" -> SEJONG
                "경기" -> GYEONGGIDO
                "강원" -> GANGWONDO
                "충북" -> CHUNGCHEONGBUKDO
                "충남" -> CHUNGCHEONGNAMDO
                "전북" -> JEOLLABUKDO
                "전남" -> JEOLLANAMDO
                "경북" -> GYEONGSANGBUKDO
                "경남" -> GYEONGSANGNAMDO
                "제주특별자치도" -> JEJU
                else -> throw IllegalArgumentException()
            }
        }
    }
}