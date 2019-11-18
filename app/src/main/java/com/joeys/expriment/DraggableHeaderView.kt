package com.joeys.expriment

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.provider.CalendarContract
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.joeys.expriment.utils.log

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
        setMeasuredDimension(width, width)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawLine(
                0f,
                0f,
                measuredWidth.toFloat(),
                measuredHeight.toFloat(),
                paint

        )
        canvas?.drawLine(
                measuredWidth.toFloat(),
                0f,
                0f,
                measuredHeight.toFloat(),
                paint

        )


    }

}