package com.joeys.router.compiler

import com.bennyhuo.aptutils.logger.Logger
import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.inject.InjectMethodBuilder
import com.joeys.router.compiler.method.StartMethodBuilder
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.javapoet.TypeSpec
import  javax.lang.model.element.Modifier
import javax.annotation.processing.Filer

class ActivityClassBuilder(private val activityClass: ActivityClass) {

    companion object {
        const val POSIX = "Builder"
        const val METHOD_NAME = "start"
    }

    fun build(filer: Filer) {

        if (activityClass.isAbtract) return

        val typeSpecBuilder = TypeSpec.classBuilder(activityClass.simpleName + POSIX)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)

        ConstantBuilder(activityClass).build(typeSpecBuilder)
        StartMethodBuilder(activityClass).build(typeSpecBuilder)
        InjectMethodBuilder(activityClass).build(typeSpecBuilder)


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