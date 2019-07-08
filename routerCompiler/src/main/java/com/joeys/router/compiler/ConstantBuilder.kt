package com.joeys.router.compiler

import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.utils.camelToUnderLine
import com.squareup.javapoet.TypeSpec
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.TypeName
import javax.lang.model.element.Modifier

class ConstantBuilder(private val activityClass: ActivityClass) {


    fun build(typebuilder: TypeSpec.Builder) {


        activityClass.fileds.forEach { field ->
            //type= TypeName.get(field.symbol.type)
            typebuilder.addField(
                    FieldSpec.builder(
                            String::class.java,
                           field.prefix + field.name.camelToUnderLine().toUpperCase(),
                            Modifier.PUBLIC,
                            Modifier.STATIC,
                            Modifier.FINAL
                    ).initializer("\$S", field.name)//赋值
                            .build()
            )


        }

    }

}