package com.joeys.expriment.view

import android.content.Context
import android.graphics.Color
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joeys.expriment.R
import com.joeys.expriment.utils.log
import kotlinx.android.synthetic.main.bottom_dialog_cancel.*

class BottomDialog(context: Context) : BottomSheetDialog(context) {


    var editContent: TextView? = null

    init {

        window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)

        val view = LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null)
        setContentView(view)


        val coordinatorLayout = view.parent.parent as? CoordinatorLayout
        coordinatorLayout?.let {

            val cancel = LayoutInflater.from(context).inflate(R.layout.bottom_dialog_cancel, null)
            editContent = cancel.findViewById(R.id.edit_content)
            cancel.setOnClickListener {
                showEditDialog(editContent?.text.toString())
            }
            val lp = CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            lp.gravity = Gravity.BOTTOM
            lp.behavior = BottomCancelBehavior()

            it.addView(cancel, -1, lp)
            for (i in 0 until it.childCount) {
                it.getChildAt(i).javaClass.simpleName.log()
            }
        }

    }

    private fun showEditDialog(initText: String? = null) {
        EditDialog(context,initText) {
            editContent?.text = it
        }.show()
    }


}