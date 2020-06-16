package com.joeys.ipc

interface IResult {

    fun isSuccess(): Boolean

    fun getCode(): Int

    fun data(): String
}