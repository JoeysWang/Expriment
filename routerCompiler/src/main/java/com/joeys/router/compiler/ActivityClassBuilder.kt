package com.joeys.router.compiler

import com.joeys.router.compiler.activity.ActivityClass
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.javapoet.TypeSpec
import  javax.lang.model.element.Modifier
import javax.annotation.processing.Filer

class ActivityClassBuilder(private val activityClass: ActivityClass) {

    companion object {
        const val POSIX = "Builder"
    }

    fun build(filer: Filer) {

        if (activityClass.isAbtract) return

        val typeSpecBuilder = TypeSpec.classBuilder(activityClass.simpleName + POSIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        ConstantBuilder(activityClass).build(typeSpecBuilder)

        writeJavaFile(filer, typeSpecBuilder.build())
    }

    fun writeJavaFile(filer: Filer, typeSpec: TypeSpec) {
        try {
            JavaFile.builder(activityClass.packageName, typeSpec)
                    .build()
                    .writeTo(filer)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}