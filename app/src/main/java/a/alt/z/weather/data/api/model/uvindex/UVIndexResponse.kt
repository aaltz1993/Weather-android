package a.alt.z.weather.data.api.model.uvindex

import com.google.gson.annotations.SerializedName

data class UVIndexResponse(
    val response: UVIndexHeaderBody
)

data class UVIndexHeaderBody(
    val header: UVIndexHeader,
    val body: UVIndexBody
)

data class UVIndexHeader(
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class UVIndexBody(
    val dataType: String,
    val items: UVIndexItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class UVIndexItems(
    @SerializedName("item")
    val list: List<UVIndexItem>
)

data class UVIndexItem(
    val code: String,
    val areaNo: String,
    val date: String,
    val today: String,
    val tomorrow: String,
    @SerializedName("dayaftertomorrow")
    val dayAfterTomorrow: String,
    @SerializedName("twodaysaftertomorrow")
    val twoDaysAfterTomorrow: String
)

/*
{
   "response":{
      "header":{
         "resultCode":"00",
         "resultMsg":"NORMAL_SERVICE"
      },
      "body":{
         "dataType":"JSON",
         "items":{
            "item":[
               {
                  "code":"A07_1",
                  "areaNo":"1100000000",
                  "date":"2021101406",
                  "today":"4",
                  "tomorrow":"3",
                  "dayaftertomorrow":"5",
                  "twodaysaftertomorrow":""
               }
            ]
         },
         "pageNo":1,
         "numOfRows":10,
         "totalCount":1
      }
   }
}
 */