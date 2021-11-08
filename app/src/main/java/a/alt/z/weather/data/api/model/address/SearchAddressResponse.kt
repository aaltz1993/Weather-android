package a.alt.z.weather.data.api.model.address

import com.google.gson.annotations.SerializedName

data class SearchAddressResponse(
    val meta: SearchAddressMeta,
    val documents: List<SearchAddressDocument>
)

data class SearchAddressMeta(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("pageable_count") val pageableCount: Int,
    @SerializedName("is_end") val isEnd: Boolean
)

data class SearchAddressDocument(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("y") val latitude: Double,
    @SerializedName("x") val longitude: Double,
    @SerializedName("address_type") val addressType: String,
    val address: SearchAddressAddress?,
    @SerializedName("road_address") val roadAddress: SearchAddressRoadAddress?
)

data class SearchAddressAddress(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1depthName: String,
    @SerializedName("region_2depth_name") val region2depthName: String,
    @SerializedName("region_3depth_name") val region3depthName: String,
    @SerializedName("region_3depth_h_name") val region3depthHName: String,
    @SerializedName("h_code") val hCode: String,
    @SerializedName("b_code") val bCode: String,
    @SerializedName("mountain_yn") val mountainYn: String,
    @SerializedName("main_address_no") val mainAddressNo: String,
    @SerializedName("sub_address_no") val subAddressNo: String,
    @SerializedName("y") val latitude: Double,
    @SerializedName("x") val longitude: Double
)

data class SearchAddressRoadAddress(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1depthName: String,
    @SerializedName("region_2depth_name") val region2depthName: String,
    @SerializedName("region_3depth_name") val region3depthName: String,
    @SerializedName("road_name") val roadName: String,
    @SerializedName("underground_yn") val undergroundYn: String,
    @SerializedName("main_building_no") val mainBuildingNo: String,
    @SerializedName("sub_building_no") val subBuildingNo: String,
    @SerializedName("building_name") val buildingName: String,
    @SerializedName("zone_no") val zoneNo: String,
    @SerializedName("y") val latitude: Double,
    @SerializedName("x") val longitude: Double
)