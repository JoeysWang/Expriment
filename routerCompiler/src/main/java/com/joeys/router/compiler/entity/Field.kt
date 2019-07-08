package com.joeys.router.compiler.entity

import com.bennyhuo.aptutils.types.asJavaTypeName
import com.squareup.javapoet.TypeName
import com.sun.tools.javac.code.Symbol

open class Field(
     private val symbol: Symbol.VarSymbol
) : Comparable<Field> {

    val name = symbol.qualifiedName.toString()

    open val prefix = "REQUIRED_"

    val isPrivate = symbol.isPrivate

    /**
     * 是否为基本类型
     */
    val isPrimitve = symbol.type.isPrimitive



    fun asJavaTypeName():TypeName{
        return symbol.type.asJavaTypeName()
    }

    override fun compareTo(other: Field): Int {
        return name.compareTo(other.name)
    }

    override fun toString(): String {
        return "Field(symbol=$symbol, name='$name', prefix='$prefix', isPrivate=$isPrivate, isPrimitve=$isPrimitve)"
    }


}