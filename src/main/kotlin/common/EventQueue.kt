package common

import common.event.Event

class EventQueue {
    private val arrayDeque = ArrayDeque<Event>()
    private val lock = Any()

    fun addEvent(event: Event) {
        synchronized(lock) {
            arrayDeque.add(event)
        }
    }

    fun runEvents() {
        synchronized(lock) {
        while (arrayDeque.isNotEmpty()) {
                val event = arrayDeque.removeFirstOrNull()
                event!!.run()
            }
        }
    }
}