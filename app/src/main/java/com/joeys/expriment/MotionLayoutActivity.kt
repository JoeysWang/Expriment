package com.joeys.expriment

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_motion_layout.*

class MotionLayoutActivity : AppCompatActivity() {
    private lateinit var mButton: View
    private lateinit var mViewpager: ViewPager
    private lateinit var mMotionLayout: MotionLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_motion_layout)

        mButton = findViewById<View>(R.id.button)
        mViewpager = findViewById<ViewPager>(R.id.viewpager)
        mMotionLayout = findViewById(R.id.motionLayout)


        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addPage("Page 1", R.layout.motion_16_viewpager_page1)
        adapter.addPage("Page 2", R.layout.motion_16_viewpager_page2)
        adapter.addPage("Page 3", R.layout.motion_16_viewpager_page3)
        mViewpager.adapter = adapter

        mViewpager.addOnPageChangeListener(object :ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val numPages = 3
                mMotionLayout.progress = (position + positionOffset) / (numPages - 1)
            }

            override fun onPageSelected(position: Int) {
            }

        })

    }
}
