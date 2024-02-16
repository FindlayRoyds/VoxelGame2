package common.event.clientevents

import common.event.ClientEvent

class ReceiveChatEvent(private val message: String) : ClientEvent() {
    override fun event() {
        println("> $message")
    }
}