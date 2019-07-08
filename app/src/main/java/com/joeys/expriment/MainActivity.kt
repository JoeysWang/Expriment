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
import com.joeys.expriment.utils.log
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay

import java.util.concurrent.atomic.AtomicBoolean

@Builder
class MainActivity : AppCompatActivity() {

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {

            R.id.navigation_home -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }


    @Required
    lateinit var userName: String

    @Required
    var userAge: Int = 0


    private lateinit var mViewpager: ViewPager

    private lateinit var adapter: Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



        findViewById<Button>(R.id.btn_to_emotion)
                .setOnClickListener {

                }



    }


    class RequstTask<T> private constructor() {


        companion object {
            private val INSTANCE: RequstTask<Any>
                    by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED)
                    {
                        RequstTask<Any>()
                    }

            fun <T> requstQuene(request: Observable<T>) {
                RequstTask<T>()

            }

        }
    }

    class RequstManager<T> {

        private val isRequsting = AtomicBoolean(false)
        private val hasValue = AtomicBoolean(false)
        private val mWaitingPublisher = PublishSubject.create<T>()

        fun request(observable: Observable<T>): Observable<T> {

            if (!hasValue.get() && isRequsting.compareAndSet(false, true)) {
                return observable.doOnNext {
                    mWaitingPublisher.onNext(it)
                    isRequsting.set(false)
                }
            } else {
                return mWaitingPublisher
                        .doOnNext {
                            hasValue.set(true)
                        }
            }

        }

    }


}
