package com.joeys.analytics.events

import com.joeys.analytics.AnalyticsKit
import com.joeys.analytics.events.base.Event


interface SetUserProperties : Event {
    fun getUserProperties(kit: AnalyticsKit): MutableMap<String, Any> {
        return mutableMapOf()
    }
}