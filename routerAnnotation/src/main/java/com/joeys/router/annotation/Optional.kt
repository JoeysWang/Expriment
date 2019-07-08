package com.joeys.router.annotation

import com.sun.org.apache.xpath.internal.operations.Bool
import java.io.Serializable
@Target(AnnotationTarget.FIELD)

@Retention(AnnotationRetention.SOURCE)
annotation class Optional
    (
    val stringValue: String = "",
    val intValue: String = "",
    val doubleValue:Double=0.0,
    val booleanValue:Boolean=true) {

}