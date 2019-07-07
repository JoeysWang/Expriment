package com.joeys.expriment

import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.EditText
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.joeys.expriment.fragments.MainFragment
import com.joeys.expriment.fragments.SecondFragment
import com.joeys.router.annotation.Builder
import com.joeys.router.annotation.Required
import androidx.lifecycle.ViewModelProviders
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

import java.util.concurrent.atomic.AtomicBoolean

@Builder
class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                mViewpager.currentItem = 0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                mViewpager.currentItem = 1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    @Required
    lateinit var username: String

    @Required
    var userage: Int = 0


    private lateinit var mViewpager: ViewPager

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)


    }


}
