package com.joeys.analytics

import com.joeys.analytics.events.ContentViewEvent
import com.joeys.analytics.events.CustomEvent
import com.joeys.analytics.events.SetUserProperties
import com.joeys.analytics.events.base.Event
import com.joeys.analytics.exceptions.UnsupportedEventException

/**
 * AnalyticsDispatcher is an interface that should be implemented for every analytics service.
 * For example: FirebaseDispatcherImpl or AnswersDispatcherImpl
 *
 * @property init - initDispatcher will be called only if this property is set to *true*
 * @property kit - should be represented by a singleton of a class that extends @AnalyticsKit
 */
interface AnalyticsDispatcher {

    val init: Boolean

    val kit: AnalyticsKit

    val dispatcherName: String

    /**
     * Should call the analytics library's initiation methods
     */
    fun initDispatcher()

    fun trackContentView(contentView: ContentViewEvent)

    fun trackCustomEvent(event: CustomEvent)

    fun setUserProperties(properties: SetUserProperties)

    /**
     * This method is called from the parent @Analytics class for each event.
     * Override this method if you plan on interfacing your own event types.
     */
    fun track(event: Event) {
        // track the event only if it is not configured as excluded
        if (event.isConsideredIncluded(kit)) {

            var handled = false

            // track for each type differently, including multiple implementations
            if (event is CustomEvent) {
                trackCustomEvent(event)
                handled = true
            }

            if (event is ContentViewEvent) {
                trackContentView(event)
                handled = true
            }

            if (event is SetUserProperties) {
                setUserProperties(event)
                handled = true
            }

            if (!handled) {
                throw UnsupportedEventException(event)
            }

        }
    }


}

