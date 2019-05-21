package com.joeys.expriment

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.joeys.analytics.Analytics
import com.joeys.analytics.AnalyticsSettings
import com.joeys.expriment.analytic.DemoAnalyticDispatcher
import com.joeys.rxmodelstore.RxModelStore
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.util.concurrent.atomic.AtomicBoolean

class MainActivity : AppCompatActivity() {

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



        val modelState = RxModelStore(UserEntity("joeys", 1))
        val d = modelState.modelState()
                .subscribe {

                }

        Analytics(AnalyticsSettings(), DemoAnalyticDispatcher(true))


        val connectable = Observable.just("a")
                .publish()
                .refCount()
        val dispose = connectable.replay(1)
                .subscribe {

                }


//        connectable.connect()


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
