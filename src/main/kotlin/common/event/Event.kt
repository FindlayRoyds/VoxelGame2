package common.event

import java.io.Serializable
import java.net.Socket

abstract class Event : Serializable {
    var socket: Socket? = null

    open fun run() {
        event()
    }

    abstract fun event()
}