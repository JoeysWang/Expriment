package com.joeys.expriment.analytic

import com.joeys.analytics.AnalyticsKit

class DemoAnalyticKit private constructor(): AnalyticsKit {
    override val name = "demo"


    private object Holder {
        val INSTANCE = DemoAnalyticKit()
    }

    companion object {
        val instance: DemoAnalyticKit by lazy { Holder.INSTANCE }
    }

}