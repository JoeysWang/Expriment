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

        setExitSharedElementCallback(createSharedElementReenterCallback(this))

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = Adapte(this) { url, view ->

            TransitionDestActivity.start(this, view, url)
        }


    }

    fun createSharedElementReenterCallback(
            context: Context
    ): android.app.SharedElementCallback {
        val shotTransitionName = context.getString(R.string.transition_shot)
        val shotBackgroundTransitionName =
                context.getString(R.string.transition_shot_background)
        return object : android.app.SharedElementCallback() {

            /**
             * We're performing a slightly unusual shared element transition i.e. from one view
             * (image in the grid) to two views (the image & also the background of the details
             * view, to produce the expand effect). After changing orientation, the transition
             * system seems unable to map both shared elements (only seems to map the shot, not
             * the background) so in this situation we manually map the background to the
             * same view.
             */
            override fun onMapSharedElements(
                    names: List<String>,
                    sharedElements: MutableMap<String, View>
            ) {
                if (sharedElements.size != names.size) {
                    // couldn't map all shared elements
                    sharedElements[shotTransitionName]?.let {
                        // has shot so add shot background, mapped to same view
                        sharedElements[shotBackgroundTransitionName] = it
                    }
                }
            }
        }
    }

    class Adapte(val context: Activity,
                 val click: (String, View) -> Unit
    ) : RecyclerView.Adapter<VH>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
            val vh = VH(LayoutInflater.from(context).inflate(R.layout.img_item, parent, false))
            vh.mIv.layoutParams.height = ScreenUtils.getScreenWidth()
            return vh
        }

        override fun getItemCount(): Int {
            return images.size
        }

        override fun onBindViewHolder(holder: VH, position: Int) {
            Glide.with(holder.mIv)
                    .load(images[position])
                    .into(holder.mIv)

            holder.mIv.setOnClickListener {
                click.invoke(images[position], holder.mIv)
            }
        }

    }

    companion object {
        val images = listOf("https://video.zhongyi.io/image/default/06932029A8F144FDA282407641CBB817-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/E63845F5CD6B41AE8917DFF7E57C526B-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/14907AF50B574EC59A4387CE05C26DA1-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/D69BBDAEA5274858AFCFC891B5E5B6B1-6-2.jpg?osize_1080x810",
                "https://video.zhongyi.io/image/default/06932029A8F144FDA282407641CBB817-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/E63845F5CD6B41AE8917DFF7E57C526B-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/14907AF50B574EC59A4387CE05C26DA1-6-2.jpg?osize_1080x1080",
                "https://video.zhongyi.io/image/default/D69BBDAEA5274858AFCFC891B5E5B6B1-6-2.jpg?osize_1080x810"

        )
    }

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val mIv = view.findViewById<ImageView>(R.id.iv)
    }


    data class Item(
            val id: Int
    )
}
