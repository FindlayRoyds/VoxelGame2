package common.event.serverevents

import common.event.ServerEvent
import common.event.clientevents.ReceiveChatEvent

class SendChatEvent(private val message: String) : ServerEvent() {
    override fun event() {
        val receiveChatEvent = ReceiveChatEvent("${player!!.username}: $message")
        server!!.serverNetwork.sendEventToEveryoneExcluding(receiveChatEvent, player!!)
    }
}