package a.alt.z.weather.utils

import kotlin.math.*

object LambertConformalConicProjection {

    private const val RE = 6371.00877
    private const val GRID = 5.0
    private const val SLAT1 = 30.0
    private const val SLAT2 = 60.0
    private const val OLAT = 38.0
    private const val OLON = 126.0
    private const val XO = 210 / GRID
    private const val YO = 675 / GRID
    private const val DEGRAD = PI / 180.0

    fun transform(latitude: Double, longitude: Double): Grid {
        val re = RE / GRID
        val slat1 = SLAT1 * DEGRAD
        val slat2 = SLAT2 * DEGRAD
        val olat = OLAT * DEGRAD
        val olon = OLON * DEGRAD

        val sn = log10(cos(slat1) / cos(slat2)) / log10(tan(PI * 0.25 + slat2 * 0.5) / tan(PI * 0.25 + slat1 * 0.5))
        val sf = tan(PI * 0.25 + slat1 * 0.5).pow(sn) * cos(slat1) / sn
        val ro = re * sf / tan(PI * 0.25 + olat * 0.5).pow(sn)
        val ra = re * sf / tan(PI * 0.25 + latitude * DEGRAD * 0.5).pow(sn)
        var theta = longitude * DEGRAD - olon
        if (theta > PI) {
            theta -= 2.0 * PI
        } else if (theta < -PI) {
            theta += 2.0 * PI
        }
        theta *= sn

        val x = ((ra * sin(theta)).toFloat() + XO + 1.5).toInt()
        val y = ((ro - ra * cos(theta)).toFloat() + YO + 1.5).toInt()

        return Grid(x, y)
    }
}

data class Grid(val x: Int, val y: Int)