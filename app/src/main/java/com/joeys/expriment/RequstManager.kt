package com.joeys.expriment

import com.jakewharton.rxrelay2.ReplayRelay
import io.reactivex.Observable
import java.util.concurrent.atomic.AtomicBoolean

class RequstManager<T> {

    private var lastBuffTime = -1


    private val isRequsting = AtomicBoolean(false)
    private val hasValue = AtomicBoolean(false)

    //    private var mWaitingEmitter: ObservableEmitter<T>? = null
    private val mWaitingPublisher = ReplayRelay.create<T>()

    fun request(observable: Observable<T>): Observable<T> {
        if (!hasValue.get() && isRequsting.compareAndSet(false, true)) {
            val dis = observable.doOnNext {
                mWaitingPublisher.accept(it)
                hasValue.set(true)
                isRequsting.set(false)
            }.doOnComplete {
            }.subscribe(mWaitingPublisher)
        }
        return mWaitingPublisher
    }
}