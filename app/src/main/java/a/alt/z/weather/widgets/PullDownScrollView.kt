package a.alt.z.weather.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class PullDownScrollView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : ScrollView(context, attrs) {

    private var rawY = 0F
    private var dY = 0F
    private var intercept = false

    var onPullDown: ((Float) -> Unit)? = null
    var onActionUp: ((Float) -> Unit)? = null

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (scrollY == 0) {
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    rawY = event.rawY
                }
                MotionEvent.ACTION_MOVE -> {
                    dY = event.rawY - rawY
                    rawY = event.rawY

                    return when {
                        intercept -> {
                            onPullDown?.invoke(dY / resources.displayMetrics.heightPixels)
                            true
                        }
                        dY > 0 -> {
                            intercept = true
                            onPullDown?.invoke(dY / resources.displayMetrics.heightPixels)
                            true
                        }
                        else -> {
                            super.onTouchEvent(event)
                        }
                    }
                }
                MotionEvent.ACTION_UP -> {
                    onActionUp?.invoke(dY)
                    rawY = 0F
                    dY = 0F
                    intercept = false
                }
                MotionEvent.ACTION_CANCEL -> {
                    onActionUp?.invoke(dY)
                    rawY = 0F
                    dY = 0F
                    intercept = false
                }
            }
        }

        return super.onTouchEvent(event)
    }
}