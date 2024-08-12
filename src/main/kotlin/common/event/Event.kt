package common.event

abstract class Event {
    open fun run() {
        event()
    }

    abstract fun event()
}