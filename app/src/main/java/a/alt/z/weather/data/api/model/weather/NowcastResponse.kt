package a.alt.z.weather.data.api.model.weather

import com.google.gson.annotations.SerializedName

data class NowcastResponse(
    val response: NowcastHeaderBody
)

data class NowcastHeaderBody(
    val header: NowcastHeader,
    val body: NowcastBody
)

data class NowcastHeader(
    val resultCode: Int,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class NowcastBody(
    val dataType: String,
    val items: NowcastItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class NowcastItems(
    @SerializedName("item")
    val list: List<NowcastItem>
)

data class NowcastItem(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    @SerializedName("obsrValue") val observedValue: String
)
/*
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"PTY",
  "nx":60,
  "ny":127,
  "obsrValue":"0"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"REH",
  "nx":60,
  "ny":127,
  "obsrValue":"78"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"RN1",
  "nx":60,
  "ny":127,
  "obsrValue":"0"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"T1H",
  "nx":60,
  "ny":127,
  "obsrValue":"22.5"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"UUU",
  "nx":60,
  "ny":127,
  "obsrValue":"-0.2"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"VEC",
  "nx":60,
  "ny":127,
  "obsrValue":"13"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"VVV",
  "nx":60,
  "ny":127,
  "obsrValue":"-1.2"
},
{
  "baseDate":"20210909",
  "baseTime":"2200",
  "category":"WSD",
  "nx":60,
  "ny":127,
  "obsrValue":"1.3"
}
 */