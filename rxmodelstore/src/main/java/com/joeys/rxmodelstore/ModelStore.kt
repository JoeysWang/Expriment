package com.joeys.rxmodelstore

import io.reactivex.Observable

interface ModelStore<T> {

    fun process(reducer: Reducer<T>)

    fun modelState(): Observable<T>
}