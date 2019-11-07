package com.joeys.expriment

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.joeys.expriment.view.BottomDialog
import com.joeys.expriment.view.BottomDialogFragment

class MainActivity : AppCompatActivity() {

    private lateinit var button: Button


    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button = findViewById<Button>(R.id.btn_to_emotion)
        button.setOnClickListener {
            BottomDialogFragment()
                    .show(supportFragmentManager, "bf")
        }
    }


}
