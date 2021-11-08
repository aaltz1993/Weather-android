package a.alt.z.weather.data.api.model.address

import com.google.gson.annotations.SerializedName

data class CoordinateToAddressResponse(
    val meta: CoordinateToAddressMeta,
    val documents: List<CoordinateToAddressDocument>
)

data class CoordinateToAddressMeta(
    @SerializedName("total_count") val totalCount: Int
)

data class CoordinateToAddressDocument(
    @SerializedName("road_address")
    val roadAddress: CoordinateToAddressRoadAddress?,
    val address: CoordinateToAddressAddress
)

data class CoordinateToAddressRoadAddress(
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
)

data class CoordinateToAddressAddress(
    @SerializedName("address_name") val addressName: String,
    @SerializedName("region_1depth_name") val region1depthName: String,
    @SerializedName("region_2depth_name") val region2depthName: String,
    @SerializedName("region_3depth_name") val region3depthName: String,
    @SerializedName("mountain_yn") val mountainYn: String,
    @SerializedName("main_address_no") val mainAddressNo: String,
    @SerializedName("sub_address_no") val subAddressNo: String
)