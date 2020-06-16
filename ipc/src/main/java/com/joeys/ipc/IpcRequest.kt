package com.joeys.ipc

class IpcRequest(private val requestK: String) : IRequest {

    private var requestCallback: ((IResult) -> Unit)? = null

    private var params: String? = null

    override fun setParams(params: String) {
        this.params = params
    }

    override fun getParams(): String {
        return params.orEmpty()
    }

    override fun getRequestKey(): String {
        return requestK
    }

    override fun addCallback(callback: (IResult) -> Unit) {
        requestCallback = callback
    }

    override fun getCallback(): ((IResult) -> Unit)? {
        return requestCallback
    }
}