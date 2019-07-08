package com.joeys.router.compiler.method

import com.joeys.router.compiler.ActivityClassBuilder
import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.entity.Field
import com.joeys.router.compiler.entity.OptionalField
import com.squareup.javapoet.TypeSpec

class StartMethodBuilder(private val activityClass: ActivityClass) {


    fun build(typebuilder: TypeSpec.Builder) {

        val startMethod = StartMethod(activityClass, ActivityClassBuilder.METHOD_NAME)

        val groupedFields = activityClass.fileds.groupBy { it is OptionalField }
        val requiredFields = groupedFields[false] ?: emptyList()
        val optionalFields = groupedFields[true] ?: emptyList()

        startMethod.addAllField(requiredFields)

        val startMethodNoOptional = startMethod.copy(ActivityClassBuilder.METHOD_NAME  )
        startMethod.addAllField(optionalFields)


        startMethod.build(typebuilder)

        if (optionalFields.isNotEmpty())
            startMethodNoOptional.build(typebuilder)




    }

}