package com.joeys.expriment

import android.app.ActivityManager
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
import com.joeys.expriment.utils.DensityUtils
import com.joeys.ipc.Callback
import com.joeys.ipc.IpcManager
import com.joeys.ipc.IpcRequest
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.contracts.contract

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button


    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DensityUtils.setDensity(this)
        setContentView(R.layout.activity_main)


    }

}

