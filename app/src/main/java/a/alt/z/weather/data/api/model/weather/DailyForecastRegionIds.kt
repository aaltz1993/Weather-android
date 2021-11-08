package a.alt.z.weather.data.api.model.weather

enum class DailyForecastRegionId(val code: String) {
    SEOUL("11B00000"), INCHEON("11B00000"), GYEONGGIDO("11B00000"),
    GANGWONDOYEONGSEO("11D10000"), GANGWONDOYEONGDONG("11D20000"),
    DAEJEON("11C20000"), SEJONG("11C20000"), CHUNGCHEONGNAMDO("11C20000"),
    CHUNGCHEONGBUKDO("11C10000"),
    GWANGJU("11F20000"), JEOLLANAMDO("11F20000"), JEOLLA_BUKDO("11F10000"),
    DAEGU("11H10000"), GYEONGSANGBUKDO("11H10000"),
    BUSAN("11H20000"), ULSAN("11H20000"), GYEONGSANGNAMDO("11H20000"),
    JEJUDO("11G00000");

    companion object {

        fun addressOf(regionDepth1Name: String): DailyForecastRegionId {
            return when (regionDepth1Name) {
                "서울" -> SEOUL
                "인천" -> INCHEON
                "경기" -> GYEONGGIDO
                "강원" -> GANGWONDOYEONGDONG
                "대전" -> DAEJEON
                "세종특별자치시" -> SEJONG
                "충남" -> CHUNGCHEONGNAMDO
                "충북" -> CHUNGCHEONGBUKDO
                "광주" -> GWANGJU
                "전남" -> JEOLLANAMDO
                "전북" -> JEOLLA_BUKDO
                "대구" -> DAEGU
                "경북" -> GYEONGSANGBUKDO
                "부산" -> BUSAN
                "울산" -> ULSAN
                "경남" -> GYEONGSANGNAMDO
                "제주특별자치도" -> JEJUDO
                else -> throw IllegalStateException()
            }
        }
    }
}
