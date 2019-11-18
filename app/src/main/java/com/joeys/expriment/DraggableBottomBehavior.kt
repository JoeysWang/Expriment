package com.joeys.expriment

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.ViewCompat
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joeys.expriment.utils.dp
import com.joeys.expriment.utils.log

class DraggableBottomBehavior : BottomSheetBehavior<View> {

    val tag = "bottom "

    var drawerHandler: View? = null

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    init {
        peekHeight = ScreenUtils.getScreenHeight() - ScreenUtils.getScreenWidth() - 25.dp
    }

    override fun onTouchEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {
        "onTouchEvent ".log()
        return super.onTouchEvent(parent, child, event)
    }

    override fun onNestedScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dxConsumed: Int, dyConsumed: Int, dxUnconsumed: Int, dyUnconsumed: Int, type: Int, consumed: IntArray) {
        super.onNestedScroll(coordinatorLayout, child, target, dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, type, consumed)
        "onNestedScroll $tag".log()
    }

    override fun layoutDependsOn(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
        return super.layoutDependsOn(parent, child, dependency)
    }


    override fun onDependentViewChanged(parent: CoordinatorLayout, child: View, dependency: View): Boolean {
//        "onDependentViewChanged  ${dependency.javaClass.simpleName} ".log()
        return super.onDependentViewChanged(parent, child, dependency)
    }


}