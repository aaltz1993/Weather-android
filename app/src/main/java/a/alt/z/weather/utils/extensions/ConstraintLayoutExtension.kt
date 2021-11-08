package a.alt.z.weather.utils.extensions

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.constraintlayout.widget.Guideline
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager

fun ConstraintLayout.update(constraintSet: ConstraintSet = ConstraintSet(), update: ConstraintSet.() -> Unit) = constraintSet.apply {
    clone(this@update)
    update()
    applyTo(this@update)
}

fun ConstraintLayout.updateWithDelayedTransition(
    constraintSet: ConstraintSet = ConstraintSet(),
    duration: Long = 250L,
    interpolator: Interpolator = AccelerateDecelerateInterpolator(),
    update: ConstraintSet.() -> Unit
) {
    update(constraintSet, update)

    TransitionManager.beginDelayedTransition(this, ChangeBounds().apply { setDuration(duration); setInterpolator(interpolator) })
}

val Guideline.guidePercent: Float get() = (layoutParams as ConstraintLayout.LayoutParams).guidePercent