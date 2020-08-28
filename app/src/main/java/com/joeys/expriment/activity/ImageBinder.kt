package com.joeys.expriment.activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.drakeet.multitype.ItemViewBinder
import com.joeys.expriment.R

class ImageBinder : ItemViewBinder<ImageEntity, ImageBinder.VH>() {


    override fun onBindViewHolder(holder: VH, item: ImageEntity) {
    }

    override fun onCreateViewHolder(inflater: LayoutInflater, parent: ViewGroup): VH {
        return VH(inflater.inflate(R.layout.item_image_rv, parent, false))
    }


    inner class VH(view: View) : RecyclerView.ViewHolder(view)

}

class ImageEntity