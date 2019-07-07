package com.joeys.expriment.fragments

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.button.MaterialButton

import com.joeys.expriment.R
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit


class MainFragment : NavHostFragment() {
    private lateinit var mBtnFlatmap: MaterialButton
    private lateinit var mBtnParallel: MaterialButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)
        mBtnFlatmap = view.findViewById<MaterialButton>(R.id.btn_flatmap)
        mBtnParallel = view.findViewById<MaterialButton>(R.id.btn_parallel)


        mBtnFlatmap.setOnClickListener {
            runFlatmapMock()
        }

        mBtnParallel.setOnClickListener {
            runParallelMock()
        }

        return view

    }

    private fun runParallelMock() {
        val start = System.currentTimeMillis()
        val d = Flowable.range(0, 10)
            .parallel()
            .runOn(Schedulers.computation())
            .flatMap {
                Flowable.just(it.toString() + " thread is :" + Thread.currentThread().name)
                    .delay(if (it % 2 == 0) 1000L else 0, TimeUnit.MILLISECONDS)
            }
            .sequential()

            .subscribe({
                Log.d("joeys", it)
            }, { }, {
                val end = System.currentTimeMillis()
                Log.d("joeys", "time cost :${end - start}")
            })

    }

    private fun runFlatmapMock() {
        val start = System.currentTimeMillis()
        val disposable = Observable.just(1)
            .subscribeOn(Schedulers.io())
            .flatMap {
                Log.d("joeys", "just thread is :" + Thread.currentThread().name)
                return@flatMap Observable.fromIterable(listOf(1, 2, 3, 4, 5, 6, 7, 8))
                    .subscribeOn(Schedulers.computation())
                    .flatMap {
                        Observable.just(it.toString() + " thread is :" + Thread.currentThread().name)
                            .delay(if (it % 2 == 0) 1000L else 0, TimeUnit.MILLISECONDS)
                    }
            }.toList()
            .subscribe(Consumer {
                it.forEach {
                    Log.d("joeys", it)

                }
                val end = System.currentTimeMillis()
                Log.d("joeys", "time cost :${end - start}")
            })
    }


}
