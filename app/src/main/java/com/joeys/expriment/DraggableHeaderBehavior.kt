package com.joeys.expriment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joeys.expriment.utils.log

class DraggableHeaderBehavior : CoordinatorLayout.Behavior<View> {
    val tag = "header "

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(bottomSheet: View, slideOffset: Float) {
        }

        override fun onStateChanged(bottomSheet: View, newState: Int) {
            "onStateChanged $newState".log()
        }

    }
//
//    override fun onLayoutChild(parent: CoordinatorLayout, child: View, layoutDirection: Int): Boolean {
//        "${this::class.java.simpleName} onLayoutChild $tag ${child.javaClass.simpleName}".log()
//
//        child.layout(0, 0, child.measuredWidth, child.measuredHeight)
//        return super.onLayoutChild(parent, child, layoutDirection)
//    }
//
//    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
//        "${this::class.java.simpleName}  onNestedPreScroll".log()
//        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
//    }
//
//    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
//        "${this::class.java.simpleName}  onNestedScroll".log()
//        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
//    }


    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        if (dependency is DraggableBottomView) {
            val lp = dependency.layoutParams as? CoordinatorLayout.LayoutParams
            val behavior = lp?.behavior
            if (behavior is DraggableBottomBehavior) {
                behavior.removeBottomSheetCallback(bottomSheetCallback)
                behavior.addBottomSheetCallback(bottomSheetCallback)
            }
        }
        return dependency is DraggableBottomView
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        child.y = (dependency.y - child.measuredHeight)
        return super.onDependentViewChanged(parent, child, dependency)
    }


}