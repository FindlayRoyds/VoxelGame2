package common.event

import java.io.Serializable
import java.net.Socket

abstract class Event : Serializable {
    var socket: Socket? = null
    abstract fun run()
    abstract fun event()
}