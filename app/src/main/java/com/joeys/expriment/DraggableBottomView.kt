package com.joeys.expriment

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.joeys.expriment.utils.log

class DraggableBottomView : FrameLayout, CoordinatorLayout.AttachedBehavior {

    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)

    override fun getBehavior(): DraggableBottomBehavior {
        return DraggableBottomBehavior()
    }



}