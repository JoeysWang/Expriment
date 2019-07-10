package com.joeys.router.compiler.inject

import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.prebuilt.ACTIVITY
import com.joeys.router.compiler.prebuilt.BUNDLE
import com.joeys.router.compiler.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class SavedInstanceBuilder(private val activityClass: ActivityClass) {
    fun builder(typebuilder: TypeSpec.Builder) {


        val methodBuilder = MethodSpec.methodBuilder("saveState")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .addParameter(ACTIVITY.java, "instance")
                .addParameter(BUNDLE.java, "outState")
                .beginControlFlow("if(instance instanceof \$T)", activityClass.typeElement)
                .addStatement("\$T typedInstance = (\$T) instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("\$T intent = new \$T()", INTENT.java, INTENT.java)

        activityClass.fileds.forEach { field ->
            val name = field.name
            val typeName = field.asJavaTypeName()

            if (field.isPrivate) {
                var getterName = name.capitalize()
                if (!getterName.startsWith("Is"))
                    getterName = "get$getterName"

                methodBuilder.addStatement("intent.putExtra(\$S,typedInstance.\$L)", name, getterName)
            } else {
                methodBuilder.addStatement("intent.putExtra(\$L=\$LValue", name, name)
            }
        }


    }
}