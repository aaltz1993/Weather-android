package a.alt.z.weather.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class CurrentWeatherResponse(
    @SerializedName("coord") val coordinate: CurrentWeatherCoordinate,
    @SerializedName("weather") val weathers: List<CurrentWeather>,
    val main: CurrentWeatherMain,
    val wind: CurrentWeatherWind,
    val clouds: CurrentWeatherCloud,
    val rain: CurrentWeatherRain?,
    val snow: CurrentWeatherSnow?,
    val dt: Long,
    val sys: CurrentWeatherSys,
    @SerializedName("timezone") val timeZone: Int
)

data class CurrentWeatherCoordinate(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double
)

data class CurrentWeather(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)

data class CurrentWeatherMain(
    @SerializedName("temp") val temperature: Float,
    @SerializedName("feel_like") val feelLike: Float,
    @SerializedName("temp_min") val minTemperature: Float,
    @SerializedName("temp_max") val maxTemperature: Float,
    val pressure: Int,
    val humidity: Int
)

data class CurrentWeatherWind(
    val speed: Float,
    @SerializedName("deg") val degree: Int
)

data class CurrentWeatherCloud(
    val all: Int
)

data class CurrentWeatherRain(
    @SerializedName("1h") val hour: Float,
    @SerializedName("3h") val threeHour: Float
)

data class CurrentWeatherSnow(
    @SerializedName("1h") val hour: Float,
    @SerializedName("3h") val threeHour: Float
)

data class CurrentWeatherSys(
    val sunrise: Long,
    val sunset: Long
)

/*
{
  "coord": {
    "lon": -122.08,
    "lat": 37.39
  },
  "weather": [
    {
      "id": 800,
      "main": "Clear",
      "description": "clear sky",
      "icon": "01d"
    }
  ],
  "base": "stations",
  "main": {
    "temp": 282.55,
    "feels_like": 281.86,
    "temp_min": 280.37,
    "temp_max": 284.26,
    "pressure": 1023,
    "humidity": 100
  },
  "visibility": 16093,
  "wind": {
    "speed": 1.5,
    "deg": 350
  },
  "clouds": {
    "all": 1
  },
  "dt": 1560350645,
  "sys": {
    "type": 1,
    "id": 5122,
    "message": 0.0139,
    "country": "US",
    "sunrise": 1560343627,
    "sunset": 1560396563
  },
  "timezone": -25200,
  "id": 420006353,
  "name": "Mountain View",
  "cod": 200
  }
 */