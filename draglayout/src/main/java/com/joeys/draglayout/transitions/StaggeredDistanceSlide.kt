package com.joeys.draglayout.transitions


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Context
import android.transition.TransitionValues
import android.transition.Visibility
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.core.content.withStyledAttributes
import com.joeys.draglayout.R
import com.joeys.draglayout.TransitionUtils

/**
 * An alternative to [android.transition.Slide] which staggers elements by **distance**
 * rather than using start delays. That is elements start from/end at a progressively increasing
 * displacement such that they come together/move apart over the same duration as they enter/exit.
 * This can produce more cohesive choreography. The displacement factor can be controlled by the
 * `spread` attribute.
 *
 *
 * Currently only supports entering/exiting from the bottom edge.
 */
class StaggeredDistanceSlide : Visibility {

    private val spread: Int

    constructor() {
        spread = 1
    }

    @Keep
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        var tmpSpread = 1

        spread = tmpSpread
    }

    override fun onAppear(
            sceneRoot: ViewGroup,
            view: View,
            startValues: TransitionValues,
            endValues: TransitionValues
    ): Animator {
        val position = endValues.values[PROPNAME_SCREEN_LOCATION] as IntArray
        return createAnimator(view, (sceneRoot.height + position[1] * spread).toFloat(), 0f)
    }

    override fun onDisappear(
            sceneRoot: ViewGroup,
            view: View,
            startValues: TransitionValues,
            endValues: TransitionValues
    ): Animator {
        val position = endValues.values[PROPNAME_SCREEN_LOCATION] as IntArray
        return createAnimator(view, 0f, (sceneRoot.height + position[1] * spread).toFloat())
    }

    private fun createAnimator(
            view: View,
            startTranslationY: Float,
            endTranslationY: Float
    ): Animator {
        view.translationY = startTranslationY
        val ancestralClipping = TransitionUtils.setAncestralClipping(view, false)
        val transition = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, endTranslationY)
        transition.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                TransitionUtils.restoreAncestralClipping(view, ancestralClipping)
            }
        })
        return transition
    }

    companion object {

        private const val PROPNAME_SCREEN_LOCATION = "android:visibility:screenLocation"
    }
}
