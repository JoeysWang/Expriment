package com.joeys.router.compiler.method

import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.entity.Field
import com.joeys.router.compiler.prebuilt.ACTIVITY_BUILDER
import com.joeys.router.compiler.prebuilt.CONTEXT
import com.joeys.router.compiler.prebuilt.INTENT
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier

class StartMethod(private val activityClass: ActivityClass,
                  private val name: String) {
    private val fields = arrayListOf<Field>()
    private var isStaticMethod = true

    fun staticMethod(boolean: Boolean): StartMethod {
        this.isStaticMethod = boolean
        return this
    }

    fun addAllField(fields: List<Field>) {
        this.fields += fields
    }

    fun addField(field: Field) {
        this.fields += field
    }

    fun copy(name: String) = StartMethod(activityClass, name)
            .also { it.fields.addAll(this.fields) }


    fun build(typebuilder: TypeSpec.Builder) {
        val methodBuilder = MethodSpec.methodBuilder(name)
                .addModifiers(Modifier.PUBLIC)
                .returns(TypeName.VOID)
                .addParameter(CONTEXT.java, "context")
        methodBuilder.addStatement("\$T intent=new \$T(context,\$T.class)",
                INTENT.java, INTENT.java, activityClass.typeElement)
        fields.forEach { field ->
            val name= field.name

            methodBuilder
                    .addParameter(field.asJavaTypeName(), name)
                    .addStatement("intent.putExtra(\$S,\$L)",name,name)
        }

        if (isStaticMethod)
            methodBuilder.addModifiers(Modifier.STATIC)
        else
            methodBuilder.addStatement("fillIntent(intent)")


        methodBuilder.addStatement("\$T.INSTANCE.startActivity(context,intent)",ACTIVITY_BUILDER.java)
        typebuilder.addMethod(methodBuilder.build())

    }
}