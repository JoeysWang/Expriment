package com.joeys.analytics.events

import com.joeys.analytics.AnalyticsKit
import com.joeys.analytics.events.base.Event

/**
 * Implement this interface in your class, if it is a ContentView event.
 * It will only have a name. nothing else.
 */
interface ContentViewEvent : Event {

    fun getViewName(kit : AnalyticsKit): String

}