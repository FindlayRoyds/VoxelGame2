package common.event.serverevents

import common.event.ServerEvent
import common.event.clientevents.SetupGameClientEvent
import common.player.Player

class ConnectionRequestServerEvent(private val username: String) : ServerEvent() {
    override fun event() {
        val newPlayerUserID = server!!.serverNetwork.getUserIDFromSocket(socket!!)!!
        val newPlayer = Player(newPlayerUserID, username)
        server!!.players.addPlayer(newPlayer)

        // val responseEvent = ConnectionResponseEvent(true)
        // server!!.serverNetwork.sendEventToUserID(responseEvent, newPlayerUserID)
        val setupGameEvent = SetupGameClientEvent(server!!.players, newPlayerUserID)
        server!!.serverNetwork.sendEventToPlayer(setupGameEvent, newPlayer)
    }
}