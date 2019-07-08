package com.joeys.router.compiler.entity

import com.bennyhuo.aptutils.types.asTypeMirror
import com.joeys.router.annotation.Optional
import com.sun.tools.javac.code.Symbol
import javax.lang.model.type.TypeKind

open class OptionalField(
    symbol: Symbol.VarSymbol
) : Field(symbol) {

    var defaultValue: Any? = null
        private set


    override open val prefix = "OPTIONAL_"


    init {
        val optional = symbol.getAnnotation(Optional::class.java)
        when (symbol.type.kind) {
            TypeKind.BOOLEAN ->
                defaultValue = optional.booleanValue
            TypeKind.INT, TypeKind.BYTE, TypeKind.LONG, TypeKind.CHAR ->
                defaultValue = optional.intValue
            TypeKind.DOUBLE, TypeKind.FLOAT -> {
                defaultValue = optional.doubleValue
            }

            else -> {
                if (symbol.type == String::class.java.asTypeMirror())
                    defaultValue = """"${optional.stringValue}""""
            }
        }
    }


    override fun compareTo(other: Field): Int {
        return if (other is OptionalField) {
            name.compareTo(other.name)
        } else {
            1
        }
    }


}