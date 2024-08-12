package common.event.clientevents

import common.event.ClientEvent
import kotlin.system.exitProcess

class DisconnectClientEvent : ClientEvent() {
    override fun event() {
        println("Disconnected from server")
        exitProcess(0)
    }
}