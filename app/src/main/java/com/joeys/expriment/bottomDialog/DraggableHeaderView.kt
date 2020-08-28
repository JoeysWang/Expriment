package com.joeys.expriment.bottomDialog

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout

class DraggableHeaderView : FrameLayout, CoordinatorLayout.AttachedBehavior {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    val paint = Paint().apply {
        color = Color.BLACK
    }

    override fun getBehavior(): DraggableHeaderBehavior {
        return DraggableHeaderBehavior()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val width = MeasureSpec.getSize(widthMeasureSpec)
        super.onMeasure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(width,MeasureSpec.EXACTLY))

//        setMeasuredDimension(width, width)
    }


}