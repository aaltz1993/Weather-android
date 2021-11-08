package a.alt.z.weather.model

import a.alt.z.weather.model.location.Location
import a.alt.z.weather.model.weather.DailyWeather
import a.alt.z.weather.model.weather.elements.Precipitation
import a.alt.z.weather.model.weather.elements.Sky
import org.threeten.bp.LocalDate

object StubData {

    val location = Location(
        0,
        true,
        37.560540, 127.029363,
        "서울특별시 성동구 무학봉길 60-1",
        "서울",
        "성동구",
        "하왕십리동"
    )

    val dailyWeathers = listOf(
        DailyWeather(LocalDate.now(),
            20, 27,
            Sky.OVERCAST, Sky.OVERCAST,
            Precipitation.RAIN, Precipitation.RAIN,
            60, 40
        ),
        DailyWeather(LocalDate.now().plusDays(1),
            19, 26,
            Sky.CLOUDY, Sky.CLOUDY,
            Precipitation.RAIN, Precipitation.RAIN,
            20, 20
        ),
        DailyWeather(LocalDate.now().plusDays(2),
            20, 27,
            Sky.CLEAR, Sky.CLEAR,
            Precipitation.NONE, Precipitation.NONE,
            0, 0
        ),
        DailyWeather(LocalDate.now().plusDays(3),
            23, 29,
            Sky.OVERCAST, Sky.OVERCAST,
            Precipitation.NONE, Precipitation.NONE,
            0, 0
        ),
        DailyWeather(LocalDate.now().plusDays(4),
            24, 29,
            Sky.CLEAR, Sky.CLEAR,
            Precipitation.NONE, Precipitation.NONE,
            0, 0
        ),
        DailyWeather(LocalDate.now().plusDays(5),
            22, 27,
            Sky.OVERCAST, Sky.OVERCAST,
            Precipitation.NONE, Precipitation.NONE,
            0, 0
        ),
        DailyWeather(LocalDate.now().plusDays(6),
            23, 28,
            Sky.CLEAR, Sky.CLEAR,
            Precipitation.NONE, Precipitation.NONE,
            0, 0
        )
    )

    /*
    val currentWeather = CurrentWeather(
        Sky.CLEAR,
        24,
        PrecipitationType.NONE,
        0F,
        WindSpeed.valueOf(0.0F),
        60,
        FineParticleGrade.GOOD,
        UltraFineParticleGrade.NORMAL
    )
     */

}