package a.alt.z.weather.data.datasource.location

import a.alt.z.weather.data.api.model.address.AddressItem
import a.alt.z.weather.data.api.service.KakaoService
import a.alt.z.weather.data.api.model.address.CoordinateToAddressResponse
import a.alt.z.weather.data.api.model.address.SearchAddressDocument
import a.alt.z.weather.data.api.model.address.SearchAddressResponse
import a.alt.z.weather.model.location.Address
import javax.inject.Inject

class LocationRemoteDataSourceImpl @Inject constructor(
    private val kakaoService: KakaoService
): LocationRemoteDataSource {

    override suspend fun searchAddress(query: String): List<AddressItem> {
        return kakaoService.searchAddress(query)
            .documents
            .mapNotNull { it.transform() }
    }

    private fun SearchAddressDocument.transform(): AddressItem? {
        return when (addressType) {
            "REGION", "REGION_ADDR" -> {
                address?.let {
                    AddressItem(
                        it.latitude, it.longitude,
                        it.addressName,
                        it.region1depthName, it.region2depthName, it.region3depthName.ifEmpty { it.region3depthHName }
                    )
                }
            }
            "ROAD", "ROAD_ADDR" -> {
                roadAddress?.let {
                    AddressItem(
                        it.latitude, it.longitude,
                        it.addressName,
                        it.region1depthName, it.region2depthName, it.region3depthName
                    )
                }
            }
            else -> null
        }
    }

    override suspend fun coordinateToAddress(latitude: Double, longitude: Double): AddressItem {
        return kakaoService.coordinateToAddress(latitude, longitude)
            .documents
            .first()
            .address
            .let {
                AddressItem(
                    latitude, longitude,
                    it.addressName,
                    it.region1depthName, it.region2depthName, it.region3depthName
                )
            }
    }
}