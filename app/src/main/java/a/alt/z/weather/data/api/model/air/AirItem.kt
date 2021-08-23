package a.alt.z.weather.data.api.model.air

data class AirItem(
        /* 아황산가스 */
        val so2Grade: Int,
        val so2Flag: String?,
        val so2Value: Double,
        /* 일산화탄소 */
        val coGrade: Int,
        val coFlag: String?,
        val coValue: Double,
        /* 오존농도 */
        val o3Grade: Int,
        val o3Flag: String?,
        val o3Value: Double,
        /* 이산화질소 */
        val no2Grade: Int,
        val no2Flag: String,
        val no2Value: Double,
        /* 미세먼지 PM10 */
        val pm10Grade: Int,
        val pm10Grade1h: Int,
        val pm10Flag: String?,
        val pm10Value: Double,
        val pm10Value24: Double,
        /* 미세먼지 PM2.5 */
        val pm25Grade: Int,
        val pm25Grade1h: Int,
        val pm25Flag: String?,
        val pm25Value: Double,
        val pm25Value24: Double,
        /* 통합대기환 */
        val khaiGrade: Int,
        val khaiValue: Int,
        val dateTime: String?
)

/*
    "so2Grade": "1",
    "so2Flag": null,
    "so2Value": "0.003",
    "coGrade": "1",
    "coFlag": null,
    "coValue": "0.4",
    "khaiGrade": "1",
    "khaiValue": "38",
    "pm10Grade": "1",
    "pm10Flag": null,
    "pm10Value": "13",
    "o3Grade": "1",
    "o3Flag": null,
    "o3Value": "0.023"
    "no2Grade": "1",
    "no2Flag": null,
    "no2Value": "0.018",
    "dataTime": "2021-08-10 09:00",
 */