package a.alt.z.weather.utils.transformations

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest
import kotlin.math.roundToInt

class PrecipitationSnowTransformation(
    private val percentage: Float
): BitmapTransformation() {

    private val min = 0.1F
    private val grade1 = 0.3F
    private val grade2 = 0.5F
    private val max = 0.7F

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        return Bitmap.createBitmap(toTransform,
            0,
            (toTransform.height * (1 - percentage)).toInt(),
            toTransform.width,
            (toTransform.height * percentage).roundToInt())
    }
}