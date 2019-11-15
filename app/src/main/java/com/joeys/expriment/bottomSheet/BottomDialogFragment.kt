package com.joeys.expriment.bottomSheet

import android.app.Dialog
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomDialogFragment() : BottomSheetDialogFragment() {


    abstract fun onCreateView(): View

    abstract fun onCreateBottomView(): View

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomDialog(
                onCreateView(),
                onCreateBottomView(),
                context!!)
    }

}