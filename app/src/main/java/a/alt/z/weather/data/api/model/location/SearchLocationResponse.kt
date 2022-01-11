package a.alt.z.weather.data.api.model.location

import com.google.gson.annotations.SerializedName

data class SearchLocationResponse(
    val documents: List<SearchLocationDocument>
)

data class SearchLocationDocument(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("road_address_name") val roadAddressName: String,
    @SerializedName("place_name") val placeName: String,
    @SerializedName("y") val latitude: Double,
    @SerializedName("x") val longitude: Double
)

/*
    {
      "address_name": "경기 이천시 대월면 장평리 산 30-1",
      "category_group_code": "AT4",
      "category_group_name": "관광명소",
      "category_name": "여행 > 관광,명소 > 산",
      "distance": "",
      "id": "7824212",
      "phone": "",
      "place_name": "대명산",
      "place_url": "http://place.map.kakao.com/7824212",
      "road_address_name": "",
      "x": "127.52867238118151",
      "y": "37.20525597039105"
    },
    {
      "address_name": "경기 여주시 가남읍 신해리",
      "category_group_code": "AT4",
      "category_group_name": "관광명소",
      "category_name": "여행 > 관광,명소 > 산",
      "distance": "",
      "id": "7824211",
      "phone": "",
      "place_name": "대명산",
      "place_url": "http://place.map.kakao.com/7824211",
      "road_address_name": "",
      "x": "127.544315227891",
      "y": "37.2247962300725"
    },
    {
      "address_name": "전남 장성군 장성읍 성산리 843-4",
      "category_group_code": "FD6",
      "category_group_name": "음식점",
      "category_name": "음식점 > 한식 > 해물,생선 > 장어",
      "distance": "",
      "id": "19538061",
      "phone": "061-392-3006",
      "place_name": "대명산장어",
      "place_url": "http://place.map.kakao.com/19538061",
      "road_address_name": "전남 장성군 장성읍 단풍로 236",
      "x": "126.801574674842",
      "y": "35.3231288154537"
    }
 */