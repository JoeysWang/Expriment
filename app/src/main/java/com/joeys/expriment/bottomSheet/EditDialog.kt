package com.joeys.expriment.bottomSheet

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatDialog
import androidx.core.widget.addTextChangedListener
import com.joeys.expriment.R
import com.joeys.expriment.utils.dp
import com.joeys.expriment.utils.log

class EditDialog(context: Context,
                 val initText: String? = null,
                 val onTextChange: (charSequence: Editable?) -> Unit
) : AppCompatDialog(context, R.style.FloatingDialog) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.edit_dialog)
        setCanceledOnTouchOutside(true)

        val layoutParams = window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = 68.dp
        layoutParams.gravity = Gravity.BOTTOM
        window!!.attributes = layoutParams

        val et = findViewById<EditText>(R.id.et)
        et?.setText(initText)
        et?.addTextChangedListener {
            onTextChange.invoke(it)
        }
        et?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                dismiss()
            }
            return@setOnEditorActionListener true
        }

        et?.post {
            et.requestFocus()
            et.setSelection(et.text.length)
            val mimm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            mimm.showSoftInput(et, 0)
        }
    }

    override fun onBackPressed() {
        "onBackPressed".log()
        super.onBackPressed()
    }

}