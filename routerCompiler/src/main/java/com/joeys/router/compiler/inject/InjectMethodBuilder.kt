package com.joeys.router.compiler.inject

import com.joeys.router.compiler.ActivityClassBuilder
import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.entity.Field
import com.joeys.router.compiler.entity.OptionalField
import com.joeys.router.compiler.prebuilt.ACTIVITY
import com.joeys.router.compiler.prebuilt.BUNDLE
import com.joeys.router.compiler.prebuilt.BUNDLE_UTILS
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class InjectMethodBuilder(private val activityClass: ActivityClass) {

    fun build(typebuilder: TypeSpec.Builder) {


        val injectBuilder = MethodSpec.methodBuilder("inject")
                .addParameter(ACTIVITY.java, "instance")
                .addParameter(BUNDLE.java, "savedInstanceState")
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                .returns(TypeName.VOID)
                .beginControlFlow("if (instance instanceof \$T)", activityClass.typeElement)
                .addStatement("\$T typeInstance = (\$T) instance", activityClass.typeElement, activityClass.typeElement)
                .addStatement("\$T extras = savedInstanceState == null? typeInstance.getIntent().getExtras() : savedInstanceState", BUNDLE.java)
                .beginControlFlow("if(extras!=null)")

        activityClass.fileds.forEach { field: Field ->
            val name = field.name
            val typeName = field.asJavaTypeName()
            if (field is OptionalField) {
                injectBuilder
                        .addStatement("\$T \$LValue=\$T.<\$T>get(extras,\$S,\$L)", typeName, name, BUNDLE_UTILS.java, typeName, name, field.defaultValue)
            } else {
                injectBuilder
                        .addStatement("\$T \$LValue=\$T.<\$T>get(extras,\$S)", typeName, name, BUNDLE_UTILS.java, typeName, name)
            }

            if (field.isPrivate) {
                var setterName = name.capitalize()
                if (setterName.startsWith("Is"))
                    setterName = setterName.removePrefix("Is")

                injectBuilder.addStatement("typeInstance.set\$L(\$LValue)", setterName, name)
            } else {
                injectBuilder.addStatement("typeInstance.\$L=\$LValue", name, name)
            }
        }

        injectBuilder.endControlFlow().endControlFlow()
        typebuilder.addMethod(injectBuilder.build())

    }


}
