package com.joeys.expriment.view

import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.joeys.expriment.utils.dp
import com.joeys.expriment.utils.log

class BottomCancelBehavior : CoordinatorLayout.Behavior<LinearLayout>() {


    override fun onLayoutChild(parent: CoordinatorLayout, child: LinearLayout, layoutDirection: Int): Boolean {

        val measuredHeight = child.measuredHeight + 28.dp
        val t = parent.bottom - measuredHeight
        val b = parent.bottom
        "BottomCancelBehavior onLayoutChild measuredHeight:$measuredHeight t$t,b$b".log()
        child.layout(0, t, parent.width, b)
        child.elevation = 100f

        return true
    }
}