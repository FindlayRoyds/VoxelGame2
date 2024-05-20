package common

import common.event.Event

class EventQueue {
    private val arrayDeque = ArrayDeque<Event>()
    private val lock = Any()
    var deltaTimeS = 0.0

    fun addEvent(event: Event) {
        synchronized(lock) {
            arrayDeque.add(event)
        }
    }

    fun runEvents(deltaTimeSIn: Double) {
        deltaTimeS = deltaTimeSIn
        synchronized(lock) {
            while (arrayDeque.isNotEmpty()) {
                val event = arrayDeque.removeFirstOrNull()
                event!!.run()
            }
        }
    }
}