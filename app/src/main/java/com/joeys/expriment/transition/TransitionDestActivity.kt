package com.joeys.expriment.transition

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Matrix
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.app.SharedElementCallback
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.blankj.utilcode.util.ScreenUtils
import com.bumptech.glide.Glide
import com.joeys.expriment.R
import com.joeys.expriment.utils.log

class TransitionDestActivity : AppCompatActivity() {
    private lateinit var mIv: ViewPager

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transition_dest)

        val h = calImgHeightByWith(810, 1080, ScreenUtils.getScreenWidth())
        mIv = findViewById(R.id.shot)
        mIv.layoutParams.height = h



    }



    fun calImgHeightByWith(with: Int, height: Int, expectWith: Int): Int {
        if (height == 0) {
            return expectWith
        }
        var h = (height * (expectWith.toFloat() / with)).toInt()
        if (expectWith > h) {
            h = expectWith
        }
        if (expectWith.toFloat() / h < 3f / 4f) {
            h = expectWith * 4 / 3
        }
        return h
    }


    companion object {
        const val image = "https://video.zhongyi.io/image/default/D855FBD67B864F6D8ECB442FBB5C293B-6-2.jpg?osize_810x1080"

        fun start(
                context: Activity,
                transitionView: View
        ) {
            val transitionName = ViewCompat.getTransitionName(transitionView) ?: ""

            val options = ActivityOptionsCompat.makeSceneTransitionAnimation(context,
                    transitionView, transitionName)

            val intent = Intent(context, TransitionDestActivity::class.java)
            context.startActivity(intent, options.toBundle())

        }
    }
}
