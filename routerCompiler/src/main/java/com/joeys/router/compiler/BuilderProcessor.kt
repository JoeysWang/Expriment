package com.joeys.router.compiler

import com.bennyhuo.aptutils.AptContext
import com.bennyhuo.aptutils.logger.Logger
import com.bennyhuo.aptutils.types.isSubTypeOf
import com.google.auto.service.AutoService
import com.joeys.router.annotation.Builder
import com.joeys.router.annotation.Optional
import com.joeys.router.annotation.Required
import com.joeys.router.compiler.activity.ActivityClass
import com.joeys.router.compiler.entity.Field
import com.joeys.router.compiler.entity.OptionalField
import com.sun.tools.javac.code.Symbol
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement

@AutoService(Processor::class) // For registering the service
@SupportedSourceVersion(SourceVersion.RELEASE_8) // to support Java 8
class BuilderProcessor : AbstractProcessor() {
    companion object {
        const val KAPT_KOTLIN_GENERATED_OPTION_NAME = "kapt.kotlin.generated"
    }

    private val supportAnnotations = setOf(
            Optional::class.java,
            Required::class.java,
            Builder::class.java
    )

    override fun process(ano: MutableSet<out TypeElement>?, env: RoundEnvironment): Boolean {
        val activityClasses = hashMapOf<Element, ActivityClass>()
        env.getElementsAnnotatedWith(Builder::class.java)
                .filter { element ->
                    element.kind.isClass
                }
                .forEach { element ->
                    try {
                        if (element.asType().isSubTypeOf("android.app.Activity")) {
                            activityClasses[element] = ActivityClass(element as TypeElement)
                        } else
                            Logger.error(element, "不支持的typeElement ${element.simpleName}")


                    } catch (e: Exception) {
                        Logger.logParsingError(element, Builder::class.java, e)
                    }

                }

        env.getElementsAnnotatedWith(Required::class.java)
                .filter { it.kind.isField }
                .forEach { element ->
                    activityClasses[element.enclosingElement]?.let {
                        it.fileds.add(Field(element as Symbol.VarSymbol))
                        val symbol = element as Symbol.VarSymbol


                    }
                            ?: Logger.error(element, "属性 $element 所在的Activity未被 ${Builder::class.java.name} 标注")


                }
        env.getElementsAnnotatedWith(Optional::class.java)
                .filter { it.kind.isField }
                .forEach { element ->
                    activityClasses[element.enclosingElement]?.let {
                        it.fileds.add(OptionalField(element as Symbol.VarSymbol))
                    }
                            ?: Logger.error(element, "属性 $element 所在的Activity未被 ${Builder::class.java.name} 标注")
                }

        Logger.warn(activityClasses.toString())
        activityClasses.forEach { element, activityClass ->
            val classBuilder = ActivityClassBuilder(activityClass)
            classBuilder.build(AptContext.filer)
        }
        return true
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return supportAnnotations.mapTo(
                hashSetOf(), {
            it.canonicalName
        }
        )
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        AptContext.init(processingEnv)
    }
}