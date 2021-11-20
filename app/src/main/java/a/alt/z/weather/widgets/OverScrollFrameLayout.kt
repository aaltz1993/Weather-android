package a.alt.z.weather.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.FrameLayout
import timber.log.Timber
import timber.log.debug
import kotlin.math.abs

class OverScrollFrameLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private var rawX: Float = 0F
    private var rawY: Float = 0F
    private var dX: Float = 0F
    private var dY: Float = 0F

    private var interceptPullDown = false
    private var interceptSwipeUp = false

    var onSwipe: (() -> Unit)? = null
    var onPullDown: ((Float) -> Unit)? = null
    var onSwipeUp: ((Float) -> Unit)? = null
    var onActionUp: ((Float, interceptPullDown: Boolean) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                rawX = event.rawX
                rawY = event.rawY
            }
            MotionEvent.ACTION_MOVE -> {
                dX = event.rawX - rawX
                dY = event.rawY - rawY
                rawX = event.rawX
                rawY = event.rawY
                
                if (abs(dX) > abs(dY)) {
                    if (!interceptPullDown && !interceptSwipeUp) {
                        onSwipe?.invoke()
                    }
                } else {
                    if (abs(dY) > 5F) {
                        when {
                            !interceptPullDown && !interceptSwipeUp -> {
                                if (dY > 0) {
                                    interceptPullDown = true
                                    onPullDown?.invoke(dY / resources.displayMetrics.heightPixels)
                                } else {
                                    interceptSwipeUp = true
                                    onSwipeUp?.invoke(dY / resources.displayMetrics.heightPixels)
                                }
                            }
                            interceptPullDown -> {
                                onPullDown?.invoke(dY / resources.displayMetrics.heightPixels)
                            }
                            interceptSwipeUp -> {
                                onSwipeUp?.invoke(dY / resources.displayMetrics.heightPixels)
                            }
                        }
                    }
                }
            }
            MotionEvent.ACTION_UP -> {
                onActionUp?.invoke(dY, interceptPullDown)
                rawX = 0F
                rawY = 0F
                dX = 0F
                dY = 0F
                interceptPullDown = false
                interceptSwipeUp = false
            }
        }
        return true
    }
}