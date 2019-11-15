package com.joeys.expriment.bottomSheet

import android.content.Context
import android.view.*
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.joeys.expriment.R
import com.joeys.expriment.utils.log

class BottomDialog(val rootView:View,
                   val bottomView:View?,
                   context: Context) : BottomSheetDialog(context) {

    init {
        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        setContentView(rootView)
        val coordinatorLayout = rootView.parent.parent as? CoordinatorLayout
        coordinatorLayout?.let {
            val lp = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.BOTTOM
            lp.behavior = BottomCancelBehavior()
            it.addView(bottomView, -1, lp)
            for (i in 0 until it.childCount) {
                it.getChildAt(i).javaClass.simpleName.log()
            }
        }

    }




}