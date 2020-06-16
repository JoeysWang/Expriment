package com.joeys.ipc

interface IRequest {

    fun setParams(params: String)

    fun getParams(): String

    fun getRequestKey(): String

    fun addCallback(callback: (IResult) -> Unit)

    fun getCallback(): ((IResult) -> Unit)?
}