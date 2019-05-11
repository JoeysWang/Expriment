package com.joeys.expriment.analytic

import com.joeys.analytics.AnalyticsDispatcher
import com.joeys.analytics.AnalyticsKit
import com.joeys.analytics.events.ContentViewEvent
import com.joeys.analytics.events.CustomEvent
import com.joeys.analytics.events.SetUserProperties

class DemoAnalyticDispatcher(override val init: Boolean) : AnalyticsDispatcher {


    override val kit: AnalyticsKit
        get() = DemoAnalyticKit.instance

    override val dispatcherName: String
        get() = "demo"

    override fun initDispatcher() {
    }

    override fun trackContentView(contentView: ContentViewEvent) {
    }

    override fun trackCustomEvent(event: CustomEvent) {
    }

    override fun setUserProperties(properties: SetUserProperties) {
    }


}