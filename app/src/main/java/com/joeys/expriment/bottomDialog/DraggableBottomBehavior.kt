package com.joeys.expriment.bottomDialog

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.widget.ViewUtils
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.coordinatorlayout.widget.ViewGroupUtils
import androidx.core.view.ViewCompat
import androidx.core.view.setPadding
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joeys.expriment.utils.dp
import com.joeys.expriment.utils.log

class DraggableBottomBehavior : BottomSheetBehavior<View> {

    val tag = "bottom "

    var drawerHandler: View? = null
    var tapOnDrawer = false

    constructor() : super()
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)


    init {
        peekHeight = ScreenUtils.getScreenHeight() - ScreenUtils.getScreenWidth() - 25.dp
        addBottomSheetCallback(object : BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                "onSlide $slideOffset".log()
                bottomSheet.setPadding(
                        0,
                        0,
                        0,
                        ((1f - slideOffset) * (bottomSheet.measuredHeight - peekHeight)).toInt()
                )
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
            }

        })
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                tapOnDrawer = parent.isPointInChildBounds(drawerHandler!!,
                        event.x.toInt(), event.y.toInt())
            }
        }

        if (!tapOnDrawer)
            return false
        else
            return super.onInterceptTouchEvent(parent, child, event)
    }


    override fun onTouchEvent(parent: CoordinatorLayout, child: View, event: MotionEvent): Boolean {

        if (tapOnDrawer)
            return super.onTouchEvent(parent, child, event)
        else
            return true
    }


    override fun onNestedPreScroll(coordinatorLayout: CoordinatorLayout, child: View, target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        if (tapOnDrawer)
            super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type)
    }

    override fun onNestedPreFling(coordinatorLayout: CoordinatorLayout, child: View, target: View, velocityX: Float, velocityY: Float): Boolean {
        return false
    }


}