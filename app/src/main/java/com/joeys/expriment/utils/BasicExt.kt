package com.joeys.expriment.utils

import android.content.res.Resources
import android.util.Log

fun String.log() {
    Log.d("joeys", this)
}
val Number.dp: Int get() = ( toFloat() * Resources.getSystem().displayMetrics.density).toInt()
