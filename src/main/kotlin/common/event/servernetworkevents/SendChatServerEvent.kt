package common.event.servernetworkevents

import common.event.ServerEvent
import common.event.clientevents.ReceiveChatClientEvent

class SendChatServerEvent(private val message: String) : ServerEvent() {
    override fun event() {
        val receiveChatEvent = ReceiveChatClientEvent("${player!!.username}: $message")
        server!!.serverNetwork.sendEventToEveryoneExcluding(receiveChatEvent, player!!)
    }
}