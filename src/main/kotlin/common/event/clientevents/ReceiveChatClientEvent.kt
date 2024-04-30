package common.event.clientevents

import common.event.ClientEvent

class ReceiveChatClientEvent(private val message: String) : ClientEvent() {
    override fun event() {
        println("> $message")
    }
}