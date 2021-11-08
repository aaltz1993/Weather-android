package a.alt.z.weather.data.api.model.sunrisesunset

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "response")
data class SunriseSunsetResponse(
    @Element
    val header: SunriseSunsetHeader,
    @Element
    val body: SunriseSunsetBody
)

@Xml(name = "header")
data class SunriseSunsetHeader(
    @PropertyElement
    val resultCode: String,
    @PropertyElement
    val resultMsg: String
)

@Xml(name = "body")
data class SunriseSunsetBody(
    @Element
    val items: SunriseSunsetItems,
    @PropertyElement
    val numOfRows: Int,
    @PropertyElement
    val pageNo: Int,
    @PropertyElement
    val totalCount: Int
)

@Xml(name = "items")
data class SunriseSunsetItems(
    @Element(name = "item")
    val item: List<SunriseSunsetItem>
)

@Xml
data class SunriseSunsetItem(
    @PropertyElement val aste: String?,
    @PropertyElement val astm: String?,
    @PropertyElement val civile: String?,
    @PropertyElement val civilm: String?,
    @PropertyElement val latitude: String?,
    @PropertyElement val latitudeNum: String?,
    @PropertyElement val location: String?,
    @PropertyElement val locdate: String?,
    @PropertyElement val longitude: String?,
    @PropertyElement val longitudeNum: String?,
    @PropertyElement val moonrise: String?,
    @PropertyElement val moonset: String?,
    @PropertyElement val moontransit: String?,
    @PropertyElement val naute: String?,
    @PropertyElement val nautm: String?,
    @PropertyElement val sunrise: String?,
    @PropertyElement val sunset: String?,
    @PropertyElement val suntransit: String?
) {
    constructor() : this(null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null)
}

/*
<naute>1842 </naute>
<nautm>0550 </nautm>
<sunrise>0648 </sunrise>
<sunset>1745 </sunset>
<suntransit>121624</suntransit>
 */