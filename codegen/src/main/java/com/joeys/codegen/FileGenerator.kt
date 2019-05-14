package com.joeys.codegen

import com.google.auto.service.AutoService
import com.joeys.kanno.KAnno
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Name
import javax.lang.model.element.TypeElement


@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
@SupportedOptions(FileGenerator.KAPT_KOTLIN_GENERATED_OPTION_NAME)
class FileGenerator : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(KAnno::class.java.name)
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latest()
    }


    override fun process(set: MutableSet<out TypeElement>?, env: RoundEnvironment?): Boolean {
        System.out.print("=========================  process  =========================  process")

        env?.getElementsAnnotatedWith(KAnno::class.java)
            ?.forEach {
                val className = it.simpleName.toString()
                val pack = processingEnv.elementUtils.getPackageOf(it).toString()

                generateClass(className, pack)

            }
        return true

    }

    private fun generateClass(className: String, pack: String) {
        val fileName = "JoeKanno_$className"

        val fileContent = KotlinClassBuilder(fileName, pack, "joeys greeting~~").getContent()
        val kaptKotlinGeneratedDir = processingEnv.options[KAPT_KOTLIN_GENERATED_OPTION_NAME]
        val file = File(kaptKotlinGeneratedDir, "$fileName.kt")


        file.writeText(fileContent)


    }
}