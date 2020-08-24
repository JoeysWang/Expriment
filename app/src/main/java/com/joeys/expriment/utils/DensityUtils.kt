package com.joeys.expriment.utils

import android.app.Activity
import android.app.Application
import android.content.ComponentCallbacks
import android.content.res.Configuration
import android.util.Log

//屏幕适配方案
object DensityUtils {


    private const val WIDTH = 480  //设计稿里屏幕的宽度

    private var appDensity = 0f  //像素密度
    private var appScaleDensity = 0f  //文字缩放密度

    /**
     * 调用需要在setContentView之前
     */
    fun setDensity(activity: Activity) {
        val appDm = activity.application.resources.displayMetrics

        if (appDensity == 0f) {
            //初始化屏幕密度
            appDensity = appDm.density
            appScaleDensity = appDm.scaledDensity
            activity.application.registerComponentCallbacks(object : ComponentCallbacks {
                override fun onLowMemory() {

                }

                override fun onConfigurationChanged(newConfig: Configuration?) {
                    //监听系统的文字大小变化
                    if (newConfig != null && newConfig.fontScale > 0) {
                        appScaleDensity = activity.application.resources.displayMetrics.scaledDensity
                    }
                }

            })

        }

        val targetDensity = appDm.widthPixels / WIDTH.toFloat() //目标像素密度
        val targetScaleDensity = targetDensity * (appScaleDensity / appDensity) //目标文字缩放密度
        val targetDensityDpi = (targetDensity * 160).toInt()

        val dpi =appDm.density;
        //替换activity的
        val activityDm = activity.resources.displayMetrics
        Log.d("joeys", "origin density:${activityDm.density} densityDpi:${activityDm.densityDpi}")
        Log.d("joeys",
                "targetDensity:$targetDensity  targetScaleDensity:$targetScaleDensity targetDensityDpi:$targetDensityDpi")

        activityDm.density = targetDensity
        activityDm.scaledDensity = targetScaleDensity
        activityDm.densityDpi = targetDensityDpi

    }

}