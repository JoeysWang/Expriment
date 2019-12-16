package com.joeys.expriment.transition

import android.app.Activity
import android.content.Context
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.transition.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.joeys.expriment.R
import com.joeys.expriment.utils.log
import kotlinx.android.synthetic.main.activity_transition.*

class TransitionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition)


        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapte(this)

        setExitSharedElementCallback(object:SharedElementCallback(){
            override fun onRejectSharedElements(rejectedSharedElements: MutableList<View>?) {
                "A EXIT onRejectSharedElements".log()
                super.onRejectSharedElements(rejectedSharedElements)
            }

            override fun onSharedElementEnd(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                "A EXIT onSharedElementEnd".log()
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots)
            }

            override fun onCaptureSharedElementSnapshot(sharedElement: View?, viewToGlobalMatrix: Matrix?, screenBounds: RectF?): Parcelable {
                "A EXIT onCaptureSharedElementSnapshot".log()
                return super.onCaptureSharedElementSnapshot(sharedElement, viewToGlobalMatrix, screenBounds)
            }

            override fun onSharedElementsArrived(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, listener: OnSharedElementsReadyListener?) {
                "A EXIT onSharedElementsArrived".log()
                super.onSharedElementsArrived(sharedElementNames, sharedElements, listener)
            }

            override fun onMapSharedElements(names: MutableList<String>?, sharedElements: MutableMap<String, View>?) {
                "A EXIT onMapSharedElements".log()
                super.onMapSharedElements(names, sharedElements)
            }

            override fun onCreateSnapshotView(context: Context?, snapshot: Parcelable?): View {
                "A EXIT onCreateSnapshotView".log()
                return super.onCreateSnapshotView(context, snapshot)
            }

            override fun onSharedElementStart(sharedElementNames: MutableList<String>?, sharedElements: MutableList<View>?, sharedElementSnapshots: MutableList<View>?) {
                "A EXIT onSharedElementStart".log()
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots)
            }
        })

    }

    class Adapte(val context: Activity) : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val vh = VH(LayoutInflater.from(context).inflate(R.layout.img_item, parent, false))
            vh.mIv.layoutParams.height = ScreenUtils.getScreenWidth()
            return vh
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            Glide.with(holder.mIv)
                    .load(TransitionDestActivity.image)
                    .into(holder.mIv)
            ViewCompat.setTransitionName(holder.mIv, "")

            holder.mIv.setOnClickListener {
                ViewCompat.setTransitionName(holder.mIv, "transition_view")
                TransitionDestActivity.start(context, holder.mIv)
            }
        }

    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val mIv = view.findViewById<ImageView>(R.id.iv)
    }


    data class Item(
            val id: Int
    )
}
