package a.alt.z.weather.utils.glide

import android.graphics.Bitmap
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import timber.log.Timber
import timber.log.debug
import java.security.MessageDigest
import kotlin.math.roundToInt

class PrecipitationSnowTransformation: BitmapTransformation() {

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
            (toTransform.height * .7).toInt(),
            toTransform.width,
            (toTransform.height * .3).roundToInt())
    }
}