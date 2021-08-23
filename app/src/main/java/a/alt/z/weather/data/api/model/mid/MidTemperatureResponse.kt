package a.alt.z.weather.data.api.model.mid

import com.google.gson.annotations.SerializedName

data class MidTemperatureResponse(
    val response: MidTemperatureHeaderBody
)

data class MidTemperatureHeaderBody(
    val header: MidTemperatureHeader,
    val body: MidTemperatureBody
)

data class MidTemperatureHeader(
    val resultCode: String,
    @SerializedName("resultMsg")
    val resultMessage: String
)

data class MidTemperatureBody(
    val dataType: String,
    val items: MidTemperatureItems,
    val pageNo: Int,
    val numOfRows: Int,
    val totalCount: Int
)

data class MidTemperatureItems(
    @SerializedName("item")
    val list: List<MidTemperatureItem>
)

data class MidTemperatureItem(
    @SerializedName("taMin3")
    val minTemperature3DaysAfter: Int,
    @SerializedName("taMax3")
    val maxTemperature3DaysAfter: Int,
    @SerializedName("taMin4")
    val minTemperature4DaysAfter: Int,
    @SerializedName("taMax4")
    val maxTemperature4DaysAfter: Int,
    @SerializedName("taMin5")
    val minTemperature5DaysAfter: Int,
    @SerializedName("taMax5")
    val maxTemperature5DaysAfter: Int,
    @SerializedName("taMin6")
    val minTemperature6DaysAfter: Int,
    @SerializedName("taMax6")
    val maxTemperature6DaysAfter: Int,
    @SerializedName("taMin7")
    val minTemperature7DaysAfter: Int,
    @SerializedName("taMax7")
    val maxTemperature7DaysAfter: Int,
    @SerializedName("taMin8")
    val minTemperature8DaysAfter: Int,
    @SerializedName("taMax8")
    val maxTemperature8DaysAfter: Int,
    @SerializedName("taMin9")
    val minTemperature9DaysAfter: Int,
    @SerializedName("taMax9")
    val maxTemperature9DaysAfter: Int,
    @SerializedName("taMin10")
    val minTemperature10DaysAfter: Int,
    @SerializedName("taMax10")
    val maxTemperature10DaysAfter: Int
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
                  "regId":"11B10101",
                  "taMin3":23,
                  "taMin3Low":1,
                  "taMin3High":1,
                  "taMax3":27,
                  "taMax3Low":1,
                  "taMax3High":1,
                  "taMin4":24,
                  "taMin4Low":1,
                  "taMin4High":0,
                  "taMax4":29,
                  "taMax4Low":1,
                  "taMax4High":1,
                  "taMin5":24,
                  "taMin5Low":1,
                  "taMin5High":1,
                  "taMax5":30,
                  "taMax5Low":1,
                  "taMax5High":1,
                  "taMin6":23,
                  "taMin6Low":1,
                  "taMin6High":1,
                  "taMax6":28,
                  "taMax6Low":1,
                  "taMax6High":1,
                  "taMin7":22,
                  "taMin7Low":1,
                  "taMin7High":1,
                  "taMax7":28,
                  "taMax7Low":1,
                  "taMax7High":1,
                  "taMin8":22,
                  "taMin8Low":0,
                  "taMin8High":2,
                  "taMax8":28,
                  "taMax8Low":0,
                  "taMax8High":1,
                  "taMin9":22,
                  "taMin9Low":0,
                  "taMin9High":1,
                  "taMax9":29,
                  "taMax9Low":0,
                  "taMax9High":1,
                  "taMin10":21,
                  "taMin10Low":0,
                  "taMin10High":1,
                  "taMax10":29,
                  "taMax10Low":0,
                  "taMax10High":1
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