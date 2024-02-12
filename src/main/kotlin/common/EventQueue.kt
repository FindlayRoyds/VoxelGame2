package common

import common.event.Event

class EventQueue {
    private val arrayDeque = ArrayDeque<Event>()

    fun addEvent(event: Event) {
        arrayDeque.add(event)
    }

    fun runEvents() {
        while (arrayDeque.isNotEmpty()) {
            val event = arrayDeque.removeFirstOrNull()
            event?.run()
        }
    }
}