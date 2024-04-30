package common.event

import java.io.Serializable

abstract class Event : Serializable {
    open fun run() {
        event()
    }

    abstract fun event()
}