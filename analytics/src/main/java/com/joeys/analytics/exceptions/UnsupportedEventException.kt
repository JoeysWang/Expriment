package com.joeys.analytics.exceptions

import com.joeys.analytics.events.base.Event


class UnsupportedEventException(event: Event) : UnsupportedOperationException() {
    override val message: String = "couldn't fire \"${event.javaClass.name}\" event"
}
