package common.event.serverevents

import common.event.ServerEvent
import common.event.clientevents.SetupGameEvent
import common.player.Player

class ConnectionRequestEvent(private val username: String) : ServerEvent() {
    override fun event() {
        val newPlayerUserID = server!!.serverNetwork.getUserIDFromSocket(socket!!)!!
        val newPlayer = Player(newPlayerUserID, username)
        server!!.players.addPlayer(newPlayer)

        // val responseEvent = ConnectionResponseEvent(true)
        // server!!.serverNetwork.sendEventToUserID(responseEvent, newPlayerUserID)
        val setupGameEvent = SetupGameEvent(server!!.players, newPlayerUserID)
        server!!.serverNetwork.sendEventToPlayer(setupGameEvent, newPlayer)
    }
}