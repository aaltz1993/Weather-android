package a.alt.z.weather.data.datasource.location

import a.alt.z.weather.data.api.model.address.AddressItem
import a.alt.z.weather.data.api.model.address.CoordinateToAddressResponse
import a.alt.z.weather.data.api.model.address.SearchAddressResponse

interface LocationRemoteDataSource {

    suspend fun searchAddress(query: String): List<AddressItem>

    suspend fun coordinateToAddress(latitude: Double, longitude: Double): AddressItem
}