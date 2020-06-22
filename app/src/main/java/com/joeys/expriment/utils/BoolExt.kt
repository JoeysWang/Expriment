package com.joeys.expriment.utils

sealed class BoolExt<out T>
object Otherwise : BoolExt<Nothing>()
class TransferData<T>(val data: T) : BoolExt<T>()


fun <T> Boolean.yes(block: () -> T): BoolExt<T> {
    if (this) {
        return TransferData(block.invoke())
    } else return Otherwise
}

fun <T> BoolExt<T>.otherwise(block: () -> T): BoolExt<T> {
    if (this is Otherwise) {
        block.invoke()
    } else
        Unit
    return this
}
