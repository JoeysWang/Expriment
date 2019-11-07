package com.joeys.expriment.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.joeys.expriment.R
import com.joeys.expriment.utils.log

class BottomDialogFragment() : BottomSheetDialogFragment() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomDialog(context!!)
    }

}