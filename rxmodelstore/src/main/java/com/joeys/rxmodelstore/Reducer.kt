package com.joeys.rxmodelstore

interface Reducer<T> {

    fun reduce(oldState: T): T
}