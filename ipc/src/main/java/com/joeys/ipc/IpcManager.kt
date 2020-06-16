package com.joeys.ipc

import java.util.*
import kotlin.reflect.KProperty

class IpcManager constructor() {

    private var requests = TreeSet<IRequest>()


    fun executeAsync(request: IRequest, callback: (IResult) -> Unit) {

        request.addCallback(callback)

        requests.add(request)
    }
 
    companion object {

        val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            IpcManager()
        }

    }

}