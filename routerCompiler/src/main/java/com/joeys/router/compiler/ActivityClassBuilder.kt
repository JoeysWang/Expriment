package com.joeys.router.compiler

import com.joeys.router.compiler.activity.ActivityClass
import com.squareup.javapoet.JavaFile
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.TypeSpec
import java.lang.reflect.Modifier
import javax.annotation.processing.Filer

class ActivityClassBuilder(private val activityClass: ActivityClass) {

    companion object {
        const val POSIX = "Builder"
    }

    fun build(filer: Filer) {

        if (activityClass.isAbtract) return

        val typeBuilder = TypeSpec.classBuilder(
            activityClass.simpleName + POSIX
        ).addModifiers(KModifier.PUBLIC, KModifier.FINAL)
    }

    fun writeJavaFile(filer: Filer, typeSpec: TypeSpec) {
 }

}