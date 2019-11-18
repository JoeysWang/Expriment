package com.joeys.expriment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import com.joeys.expriment.utils.log
import android.opengl.ETC1.getHeight
import androidx.core.view.ViewCompat.setY
import android.opengl.ETC1.getWidth
import androidx.core.view.ViewCompat.setX
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MotionEvent
import android.view.View
import androidx.core.view.ViewCompat
import kotlinx.android.synthetic.main.activity_quran_share.*
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.util.Log


class BehaviorActivity : AppCompatActivity() {
    private lateinit var mTop: View
    private lateinit var mBottom: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quran_share)

        mTop = findViewById(R.id.top)
        mBottom = findViewById(R.id.bottom)
    }


}
