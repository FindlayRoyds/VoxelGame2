package common

import common.event.Event

class EventQueue {
    private var arrayDeque = ArrayDeque<Event>()
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
            val copiedArrayDeque = arrayDeque
            arrayDeque = ArrayDeque<Event>()
            while (copiedArrayDeque.isNotEmpty()) {
                val event = copiedArrayDeque.removeFirstOrNull()
                event!!.run()
            }
        }
    }
}