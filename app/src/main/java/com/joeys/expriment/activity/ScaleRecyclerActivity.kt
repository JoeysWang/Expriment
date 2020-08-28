package com.joeys.expriment.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.drakeet.multitype.MultiTypeAdapter
import com.joeys.expriment.R
import kotlinx.android.synthetic.main.activity_scale_recycler.*

class ScaleRecyclerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scale_recycler)

        val binder = ImageBinder()
        val items = mutableListOf<ImageEntity>()
        for (i in 0..100) {
            items.add(ImageEntity())
        }

        val adapter = MultiTypeAdapter(items)
        adapter.register(binder)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}