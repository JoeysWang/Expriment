package com.joeys.ipc

class IpcResult() : IResult {


    var data: String = ""

    var code_: Int = 0
    var success: Boolean = false

    override fun isSuccess(): Boolean {
        return success
    }

    override fun getCode(): Int {
        return code_
    }

    override fun data(): String {
        return data
    }
}