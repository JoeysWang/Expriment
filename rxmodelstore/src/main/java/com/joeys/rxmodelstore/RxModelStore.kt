package com.joeys.rxmodelstore

import android.annotation.SuppressLint
import com.jakewharton.rxrelay2.PublishRelay
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers

open class RxModelStore<T>(val startingState: T) : ModelStore<T> {


    private val reducers = PublishRelay.create<Reducer<T>>()

    private val store = reducers
            .observeOn(AndroidSchedulers.mainThread())
            .scan(startingState) { oldstate, reducer ->
                return@scan reducer.reduce(oldstate)
            }
            .replay(1)
            .apply { connect() }


    override fun process(reducer: Reducer<T>) {
        reducers.accept(reducer)
    }

    override fun modelState(): Observable<T> {
        return store
    }


}