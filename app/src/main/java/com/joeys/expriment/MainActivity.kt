package com.joeys.expriment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joeys.expriment.bottomSheet.BottomDialogFragment
import com.joeys.expriment.bottomSheet.EditDialog

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button


    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.btn_to_emotion)
        button.setOnClickListener {
            Btdf().show(supportFragmentManager, "bf")
        }
    }


}

class Btdf : BottomDialogFragment() {
    override fun onCreateView(): View {
        val view = LayoutInflater.from(context).inflate(R.layout.bottom_dialog, null)

        val mRv = view.findViewById<RecyclerView>(R.id.rv)

        mRv.layoutManager = LinearLayoutManager(context!!)
        mRv.adapter = MyAdapter(context!!)
        return view
    }

    override fun onCreateBottomView(): View {
        val cancel = LayoutInflater.from(context).inflate(R.layout.bottom_dialog_cancel, null)
        cancel.setOnClickListener {
            dismiss()
        }
        return cancel
    }

}

class MyAdapter(val context: Context) : RecyclerView.Adapter<VH>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(LayoutInflater.from(context).inflate(R.layout.item_bottom_menu, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
    }

}

class VH(view: View) : RecyclerView.ViewHolder(view)