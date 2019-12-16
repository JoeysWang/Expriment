package com.joeys.expriment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.blankj.utilcode.util.ScreenUtils
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.joeys.expriment.utils.dp
import com.joeys.expriment.utils.log

class DraggableBottomView : FrameLayout, CoordinatorLayout.AttachedBehavior {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun getBehavior(): DraggableBottomBehavior {
        return DraggableBottomBehavior()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
    }

    fun dismiss() {
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
    }
//    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
//
//        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
//                ScreenUtils.getScreenHeight() - 56.dp - 25.dp - 50.dp
//        )
//
////        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
////        val parent = parent as ViewGroup
////
////        for (i in 0 until parent.childCount) {
////            val child = parent.getChildAt(i)
////            if (child is DraggableHeaderView) {
////
////            }
////        }
//
//
//    }

}