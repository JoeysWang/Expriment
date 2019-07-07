package com.joeys.router.compiler.activity

import com.bennyhuo.aptutils.types.packageName
import com.bennyhuo.aptutils.types.simpleName
import com.joeys.router.compiler.entity.Field
import java.util.*
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement

class ActivityClass(val typeElement: TypeElement) {

    val simpleName: String = typeElement.simpleName()
    val packageName: String = typeElement.packageName()
    val fileds = TreeSet<Field>()


    val isAbtract = typeElement.modifiers.contains(Modifier.ABSTRACT)

    val isKotlin = typeElement.getAnnotation(Metadata::class.java) != null

    override fun toString(): String {
        return "ActivityClass(typeElement=$typeElement, simpleName='$simpleName', packageName='$packageName', fileds=${fileds.toString()}, isAbtract=$isAbtract, isKotlin=$isKotlin)"
    }


}

fun TreeSet<Any>.toString(): String {

    val i = this.iterator()
    if (!i.hasNext())
        return "[]"

    val sb = StringBuilder()
    sb.append('[')
    while (true) {
        val e = i.next()
        sb.append(e.toString())
        if (!i.hasNext())
            return sb.append(']').toString()
        sb.append(',').append(' ')
    }
}